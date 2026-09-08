'use client';

import React, { useState, useMemo } from 'react';
import { useApp } from '@/context/AppContext';
import { ExerciseQuestion, MnemonicWord } from '@/types';
import {
  CheckCircle2,
  XCircle,
  ArrowRight,
  RotateCcw,
  Sparkles,
  Volume2,
  Trophy,
  Zap,
  BookOpen,
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { audioManager } from '@/utils/audio';

interface PracticeQuestionItem {
  id: string;
  mode: 'TARGET_TO_UZBEK' | 'UZBEK_TO_TARGET';
  prompt: string;
  displayWord: string;
  displaySub: string;
  options: string[];
  correctIndex: number;
  correctAnswer: string;
  word: MnemonicWord;
  explanation: string;
}

export const PracticeView: React.FC = () => {
  const {
    targetLanguage,
    activeWords,
    speakWord,
    recordExerciseResult,
    setActiveTab,
  } = useApp();

  const [questionIndex, setQuestionIndex] = useState<number>(0);
  const [selectedOption, setSelectedOption] = useState<number | null>(null);
  const [isAnswered, setIsAnswered] = useState<boolean>(false);
  const [score, setScore] = useState<number>(0);
  const [isFinished, setIsFinished] = useState<boolean>(false);
  const [shuffleKey, setShuffleKey] = useState<number>(0);

  // Generate 5 daily interactive questions from activeWords
  // Guaranteed: Target -> Uzbek or Uzbek -> Target, never showing target word in question AND only target words in options
  const questions: PracticeQuestionItem[] = useMemo(() => {
    if (activeWords.length === 0) return [];
    const pool = [...activeWords];
    const shuffled = pool.sort(() => Math.random() - 0.5);
    const count = Math.min(5, shuffled.length);
    const result: PracticeQuestionItem[] = [];

    const langName = targetLanguage === 'ru' ? 'ruscha' : 'inglizcha';
    const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';

    for (let i = 0; i < count; i++) {
      const target = shuffled[i];
      const distractors = pool.filter(w => w.id !== target.id).sort(() => Math.random() - 0.5);
      
      // Alternate cleanly between:
      // Mode A (Even): Foreign word -> 4 Uzbek options
      // Mode B (Odd): Uzbek word -> 4 Foreign word options
      const isTargetToUzbek = i % 2 === 0;

      if (isTargetToUzbek) {
        // Mode 1: Target word displayed -> 4 Uzbek options
        const prompt = `Ushbu ${langName} so'zining to'g'ri o'zbekcha tarjimasini toping:`;
        const correctAnswer = target.uzbekMeaning;
        const optionsList: string[] = [];

        for (let d = 0; d < Math.min(3, distractors.length); d++) {
          optionsList.push(distractors[d].uzbekMeaning);
        }

        const insertIdx = Math.floor(Math.random() * (optionsList.length + 1));
        optionsList.splice(insertIdx, 0, correctAnswer);

        result.push({
          id: `q_${i}_${target.id}`,
          mode: 'TARGET_TO_UZBEK',
          prompt,
          displayWord: target.word,
          displaySub: target.pronunciation,
          options: optionsList,
          correctIndex: insertIdx,
          correctAnswer,
          word: target,
          explanation: target.mnemonicStory,
        });
      } else {
        // Mode 2: Uzbek meaning displayed -> 4 Foreign word options
        const prompt = `Quyidagi o'zbekcha ma'noga mos keluvchi ${langName} so'zni toping:`;
        const correctAnswer = target.word;
        const optionsList: string[] = [];

        for (let d = 0; d < Math.min(3, distractors.length); d++) {
          optionsList.push(distractors[d].word);
        }

        const insertIdx = Math.floor(Math.random() * (optionsList.length + 1));
        optionsList.splice(insertIdx, 0, correctAnswer);

        result.push({
          id: `q_${i}_${target.id}`,
          mode: 'UZBEK_TO_TARGET',
          prompt,
          displayWord: target.uzbekMeaning,
          displaySub: `Mnemonik ilmoq: «${target.mnemonicHook}»`,
          options: optionsList,
          correctIndex: insertIdx,
          correctAnswer,
          word: target,
          explanation: target.mnemonicStory,
        });
      }
    }

    return result;
  }, [activeWords, targetLanguage, shuffleKey]);

  if (questions.length === 0) {
    return (
      <div className="p-8 text-center text-slate-400 bg-slate-900/50 rounded-2xl border border-slate-800">
        Mashqlar uchun yetarli so'zlar topilmadi.
      </div>
    );
  }

  const currentQ = questions[questionIndex];
  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';
  const langName = targetLanguage === 'ru' ? 'ruscha' : 'inglizcha';

  const autoAdvanceTimerRef = React.useRef<NodeJS.Timeout | null>(null);

  // Clear timer on unmount
  React.useEffect(() => {
    return () => {
      if (autoAdvanceTimerRef.current) {
        clearTimeout(autoAdvanceTimerRef.current);
      }
    };
  }, []);

  const handleSelectOption = (idx: number) => {
    if (isAnswered) return;

    setSelectedOption(idx);
    setIsAnswered(true);

    const isCorrect = idx === currentQ.correctIndex;

    if (isCorrect) {
      audioManager.playSuccessSound();
      setScore(prev => prev + 1);

      // Trigger Confetti
      confetti({
        particleCount: 50,
        spread: 60,
        origin: { y: 0.7 },
      });

      // Auto play audio of the foreign word
      speakWord(currentQ.word.word);

      // Auto advance after 1.8 seconds on correct
      autoAdvanceTimerRef.current = setTimeout(() => {
        handleNext();
      }, 1800);
    } else {
      audioManager.playErrorSound();
      // On incorrect answer, pronounce target word so user learns the right pronunciation
      speakWord(currentQ.word.word);
    }
  };

  const handleNext = () => {
    if (autoAdvanceTimerRef.current) {
      clearTimeout(autoAdvanceTimerRef.current);
      autoAdvanceTimerRef.current = null;
    }

    if (questionIndex + 1 < questions.length) {
      setQuestionIndex(prev => prev + 1);
      setSelectedOption(null);
      setIsAnswered(false);
    } else {
      setIsFinished(true);
      recordExerciseResult(score + (selectedOption === currentQ.correctIndex ? 1 : 0), questions.length);
    }
  };

  const handleRestart = () => {
    if (autoAdvanceTimerRef.current) {
      clearTimeout(autoAdvanceTimerRef.current);
      autoAdvanceTimerRef.current = null;
    }
    setQuestionIndex(0);
    setSelectedOption(null);
    setIsAnswered(false);
    setScore(0);
    setIsFinished(false);
    setShuffleKey(prev => prev + 1);
  };

  // Completion Result Card
  if (isFinished) {
    const finalScore = score;
    const percent = Math.round((finalScore / questions.length) * 100);

    return (
      <div className="max-w-md mx-auto p-6 sm:p-8 rounded-3xl bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-xl text-center space-y-6 animate-scale-up">
        <div className="w-20 h-20 mx-auto rounded-3xl bg-gradient-to-tr from-amber-500 to-indigo-600 p-0.5 shadow-xl shadow-indigo-500/20">
          <div className="w-full h-full bg-slate-950 rounded-[22px] flex items-center justify-center">
            <Trophy className="w-10 h-10 text-amber-400 animate-bounce" />
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="text-2xl font-black text-white">
            {percent >= 80 ? '🎉 Ajoyib Natija!' : '💪 Yaxshi Harakat!'}
          </h2>
          <p className="text-xs text-slate-300">
            {percent >= 80
              ? 'Barcha so\'zlarning mnemonik obrazlari miyangizga mustahkam yozildi!'
              : 'Xato qilingan so\'zlarning assotsiatsiyalarini qayta ko\'rib chiqing.'}
          </p>
        </div>

        <div className="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 flex items-center justify-around">
          <div>
            <div className="text-2xl font-black text-indigo-400 font-mono">
              {finalScore} / {questions.length}
            </div>
            <div className="text-[10px] text-slate-400">To'g'ri javob</div>
          </div>
          <div>
            <div className={`text-2xl font-black font-mono ${percent >= 80 ? 'text-emerald-400' : 'text-amber-400'}`}>
              {percent}%
            </div>
            <div className="text-[10px] text-slate-400">Aniqlik darajasi</div>
          </div>
        </div>

        <div className="flex flex-col gap-2.5">
          <button
            onClick={handleRestart}
            className="w-full py-3 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-bold text-sm shadow-lg shadow-indigo-500/25 hover:scale-[1.02] transition-all cursor-pointer flex items-center justify-center gap-2"
          >
            <RotateCcw className="w-4 h-4" />
            <span>Qayta mashq qilish</span>
          </button>

          <button
            onClick={() => setActiveTab('quiz')}
            className="w-full py-3 rounded-xl bg-slate-800 text-slate-200 font-semibold text-xs hover:bg-slate-700 transition-all cursor-pointer"
          >
            🎮 Tezkor Viktorina (Speed Quiz) rejimiga o'tish
          </button>
        </div>
      </div>
    );
  }

  const isSelectedCorrect = selectedOption === currentQ.correctIndex;

  return (
    <div className="max-w-2xl mx-auto space-y-6 pb-12 animate-fade-in">
      
      {/* Top Header & Progress */}
      <div className="flex items-center justify-between text-xs text-slate-400">
        <div className="flex items-center gap-2">
          <span className="font-bold text-white uppercase tracking-wider">
            {langFlag} Mustahkamlovchi Mashq
          </span>
          <span>•</span>
          <span className="font-mono text-indigo-400 font-bold">
            {questionIndex + 1} / {questions.length}
          </span>
        </div>

        <div className="flex items-center gap-1 text-emerald-400 font-mono font-bold">
          <CheckCircle2 className="w-4 h-4" />
          <span>{score} to'g'ri</span>
        </div>
      </div>

      {/* Main Question Card */}
      <div className="p-6 sm:p-8 rounded-3xl bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-xl space-y-6">
        
        {/* Question Header & Target Word highlight */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-semibold text-indigo-400 uppercase tracking-wider">
              Savol {questionIndex + 1}:
            </span>
            <span className="text-[11px] px-2.5 py-0.5 rounded-full bg-slate-800 text-slate-300 border border-slate-700 font-medium">
              {currentQ.mode === 'TARGET_TO_UZBEK' ? `${langFlag} Chet tilidan 🇺🇿 O'zbekchaga` : `🇺🇿 O'zbekchadan ${langFlag} Chet tiliga`}
            </span>
          </div>

          <h3 className="text-base sm:text-lg font-bold text-white leading-snug">
            {currentQ.prompt}
          </h3>

          <div className="p-5 rounded-2xl bg-slate-950/90 border border-slate-800 flex items-center justify-between gap-3 shadow-inner">
            <div className="space-y-1">
              <div className="flex items-center gap-2">
                <span className="text-xs font-bold px-2 py-0.5 rounded-md bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                  {currentQ.mode === 'TARGET_TO_UZBEK' ? langFlag : '🇺🇿'}
                </span>
                <span className="text-2xl sm:text-3xl font-black text-white uppercase tracking-tight font-sans">
                  {currentQ.displayWord}
                </span>
              </div>
              <div className="text-xs font-mono text-slate-400">
                {currentQ.displaySub}
              </div>
            </div>

            <button
              onClick={() => speakWord(currentQ.word.word)}
              className="p-3 rounded-2xl bg-indigo-500/20 text-indigo-400 hover:bg-indigo-500 hover:text-white transition-all cursor-pointer shadow-md"
              title="Talaffuzni tinglash"
            >
              <Volume2 className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Options List */}
        <div className="space-y-2.5">
          {currentQ.options.map((option, idx) => {
            let style = 'bg-slate-800/60 border-slate-700 text-slate-200 hover:bg-slate-750 hover:border-slate-600';
            
            if (isAnswered) {
              if (idx === currentQ.correctIndex) {
                style = 'bg-emerald-500/20 border-emerald-500 text-emerald-200 shadow-lg shadow-emerald-500/20 font-bold';
              } else if (idx === selectedOption) {
                style = 'bg-rose-500/20 border-rose-500 text-rose-200 shadow-lg shadow-rose-500/20';
              } else {
                style = 'bg-slate-900/40 border-slate-800 text-slate-500 opacity-50';
              }
            }

            return (
              <button
                key={idx}
                disabled={isAnswered}
                onClick={() => handleSelectOption(idx)}
                className={`w-full p-4 rounded-2xl border text-left text-sm font-semibold transition-all duration-200 flex items-center justify-between group cursor-pointer select-none ${style}`}
              >
                <div className="flex items-center gap-3">
                  <span className="w-7 h-7 rounded-xl bg-slate-950/60 border border-slate-700/60 flex items-center justify-center text-xs font-mono text-slate-400 group-hover:text-white">
                    {String.fromCharCode(65 + idx)}
                  </span>
                  <span className="text-base">{option}</span>
                </div>

                {isAnswered && idx === currentQ.correctIndex && (
                  <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />
                )}
                {isAnswered && idx === selectedOption && idx !== currentQ.correctIndex && (
                  <XCircle className="w-5 h-5 text-rose-400 shrink-0" />
                )}
              </button>
            );
          })}
        </div>

        {/* Mnemonic Explanation & Next Button after answering */}
        {isAnswered && (
          <div className="space-y-4 pt-4 border-t border-slate-800 animate-slide-down">
            <div className={`p-4 rounded-2xl border ${isSelectedCorrect ? 'bg-emerald-950/30 border-emerald-500/30 text-emerald-300' : 'bg-rose-950/30 border-rose-500/30 text-rose-300'}`}>
              <div className="flex items-center gap-2 text-xs font-bold mb-1">
                {isSelectedCorrect ? (
                  <>
                    <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                    <span>To'g'ri topildi! Barakalla!</span>
                  </>
                ) : (
                  <>
                    <XCircle className="w-4 h-4 text-rose-400" />
                    <span>To'g'ri javob: <strong className="text-white underline">{currentQ.correctAnswer}</strong></span>
                  </>
                )}
              </div>

              {/* Mnemonic Story for deep memory reinforcement */}
              <div className="mt-3 p-3 rounded-xl bg-slate-950/80 border border-slate-800 text-slate-300 space-y-1.5">
                <div className="flex items-center gap-2 text-xs font-bold text-amber-400">
                  <Zap className="w-3.5 h-3.5" />
                  <span>Mnemonik Ilmoq: «{currentQ.word.mnemonicHook}»</span>
                </div>
                <p className="text-xs text-slate-300 italic leading-relaxed">
                  "{currentQ.word.mnemonicStory}"
                </p>
                <div className="text-[11px] text-slate-400 pt-1 border-t border-slate-800 flex items-center justify-between">
                  <span>🇺🇿 {currentQ.word.uzbekMeaning} = {langFlag} {currentQ.word.word}</span>
                  <button
                    onClick={() => speakWord(currentQ.word.word)}
                    className="flex items-center gap-1 text-indigo-400 hover:text-indigo-300 cursor-pointer"
                  >
                    <Volume2 className="w-3.5 h-3.5" />
                    <span>Qayta tinglash</span>
                  </button>
                </div>
              </div>
            </div>

            {/* Next Button */}
            <button
              onClick={handleNext}
              className="w-full py-3.5 rounded-2xl bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white font-bold text-sm shadow-xl shadow-indigo-600/30 hover:shadow-indigo-600/50 hover:scale-[1.02] active:scale-[0.98] transition-all cursor-pointer flex items-center justify-center gap-2"
            >
              <span>{questionIndex + 1 === questions.length ? 'Natijalarni ko\'rish' : 'Keyingi savolga o\'tish'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        )}

      </div>
    </div>
  );
};
