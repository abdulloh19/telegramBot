'use client';

import React, { useState, useEffect, useCallback, useRef } from 'react';
import { useApp } from '@/context/AppContext';
import { MnemonicWord } from '@/types';
import { Zap, Volume2, RotateCcw, Award, CheckCircle2, XCircle, Clock, Sparkles, ArrowRight } from 'lucide-react';
import { audioManager } from '@/utils/audio';
import confetti from 'canvas-confetti';

interface QuizQuestion {
  word: MnemonicWord;
  mode: 'TARGET_TO_UZBEK' | 'UZBEK_TO_TARGET';
  displayTitle: string;
  displayWord: string;
  displaySub: string;
  options: string[];
  correctAnswer: string;
}

export const SpeedQuizView: React.FC = () => {
  const { activeWords, speakWord, targetLanguage, recordQuizScore } = useApp();
  
  const [questions, setQuestions] = useState<QuizQuestion[]>([]);
  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [isAnswered, setIsAnswered] = useState<boolean>(false);
  const [score, setScore] = useState<number>(0);
  const [timeLeft, setTimeLeft] = useState<number>(15);
  const [quizCompleted, setQuizCompleted] = useState<boolean>(false);
  const [streak, setStreak] = useState<number>(0);

  const timerRef = useRef<NodeJS.Timeout | null>(null);

  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';
  const langName = targetLanguage === 'ru' ? 'ruscha' : 'inglizcha';

  // Generate randomized questions from activeWords
  const generateQuestions = useCallback(() => {
    if (activeWords.length === 0) return;
    
    // Shuffle words
    const shuffled = [...activeWords].sort(() => 0.5 - Math.random());
    const selected = shuffled.slice(0, Math.min(10, shuffled.length));

    const generated: QuizQuestion[] = selected.map((targetWord, idx) => {
      const isTargetToUzbek = idx % 2 === 0;

      if (isTargetToUzbek) {
        // Target word -> 4 Uzbek options
        const distractors = activeWords
          .filter(w => w.id !== targetWord.id)
          .sort(() => 0.5 - Math.random())
          .slice(0, 3)
          .map(w => w.uzbekMeaning);

        const options = [targetWord.uzbekMeaning, ...distractors].sort(() => 0.5 - Math.random());

        return {
          word: targetWord,
          mode: 'TARGET_TO_UZBEK',
          displayTitle: `${langFlag} ${langName.toUpperCase()} SO'ZNING O'ZBEKCHA TARJIMASI:`,
          displayWord: targetWord.word,
          displaySub: targetWord.pronunciation,
          options,
          correctAnswer: targetWord.uzbekMeaning,
        };
      } else {
        // Uzbek meaning -> 4 Target options
        const distractors = activeWords
          .filter(w => w.id !== targetWord.id)
          .sort(() => 0.5 - Math.random())
          .slice(0, 3)
          .map(w => w.word);

        const options = [targetWord.word, ...distractors].sort(() => 0.5 - Math.random());

        return {
          word: targetWord,
          mode: 'UZBEK_TO_TARGET',
          displayTitle: `🇺🇿 O'ZBEKCHA SO'ZNING ${langFlag} ${langName.toUpperCase()} TARJIMASI:`,
          displayWord: targetWord.uzbekMeaning,
          displaySub: `Ilmoq: «${targetWord.mnemonicHook}»`,
          options,
          correctAnswer: targetWord.word,
        };
      }
    });

    setQuestions(generated);
    setCurrentIndex(0);
    setSelectedOption(null);
    setIsAnswered(false);
    setScore(0);
    setStreak(0);
    setTimeLeft(15);
    setQuizCompleted(false);
  }, [activeWords, targetLanguage, langFlag, langName]);

  useEffect(() => {
    generateQuestions();
  }, [generateQuestions]);

  const handleTimeUp = useCallback(() => {
    setIsAnswered(true);
    setSelectedOption('__TIME_UP__');
    audioManager.playErrorSound();
    setStreak(0);
  }, []);

  // Timer countdown
  useEffect(() => {
    if (quizCompleted || isAnswered || questions.length === 0) return;

    timerRef.current = setInterval(() => {
      setTimeLeft(prev => {
        if (prev <= 1) {
          clearInterval(timerRef.current!);
          handleTimeUp();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [currentIndex, isAnswered, quizCompleted, questions.length, handleTimeUp]);

  const autoAdvanceRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    return () => {
      if (autoAdvanceRef.current) clearTimeout(autoAdvanceRef.current);
    };
  }, []);

  const handleOptionSelect = (option: string) => {
    if (isAnswered || quizCompleted) return;

    if (timerRef.current) clearInterval(timerRef.current);
    setSelectedOption(option);
    setIsAnswered(true);

    const currentQ = questions[currentIndex];
    const isCorrect = option === currentQ.correctAnswer;

    if (isCorrect) {
      audioManager.playSuccessSound();
      setScore(prev => prev + 10 + (streak * 2));
      setStreak(prev => prev + 1);
      
      // Pronounce target word
      speakWord(currentQ.word.word);

      // Auto advance to next question after 1.4 seconds on correct answer
      autoAdvanceRef.current = setTimeout(() => {
        advanceToNext();
      }, 1400);
    } else {
      audioManager.playErrorSound();
      setStreak(0);
      speakWord(currentQ.word.word);
    }
  };

  const advanceToNext = () => {
    if (autoAdvanceRef.current) {
      clearTimeout(autoAdvanceRef.current);
      autoAdvanceRef.current = null;
    }

    if (currentIndex + 1 < questions.length) {
      setCurrentIndex(prev => prev + 1);
      setSelectedOption(null);
      setIsAnswered(false);
      setTimeLeft(15);
    } else {
      setQuizCompleted(true);
      recordQuizScore(score);
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 },
      });
    }
  };

  if (questions.length === 0) {
    return (
      <div className="p-8 text-center bg-slate-900/50 rounded-2xl border border-slate-800 text-slate-400">
        Savollar yuklanmoqda...
      </div>
    );
  }

  // Final Results Card
  if (quizCompleted) {
    return (
      <div className="max-w-md mx-auto p-6 bg-slate-900/90 border border-slate-800 rounded-3xl text-center space-y-6 shadow-2xl backdrop-blur-md animate-scaleUp">
        <div className="w-20 h-20 mx-auto bg-gradient-to-tr from-amber-400 to-orange-500 rounded-3xl p-1 shadow-lg shadow-amber-500/20">
          <div className="w-full h-full bg-slate-950 rounded-[20px] flex items-center justify-center">
            <Award className="w-10 h-10 text-amber-400 animate-bounce" />
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="text-2xl font-black text-white">Tezkor Viktorina Yakunlandi!</h2>
          <p className="text-xs text-slate-400">
            Miyangiz so&apos;zlarni chaqmoqdek tez eslab qolishini muvaffaqiyatli sinab ko&apos;rdingiz.
          </p>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div className="bg-slate-800/60 p-4 rounded-2xl border border-slate-700/50">
            <span className="text-xs text-slate-400">Jami Ball</span>
            <div className="text-3xl font-black text-amber-400 font-mono mt-1">+{score}</div>
          </div>
          <div className="bg-slate-800/60 p-4 rounded-2xl border border-slate-700/50">
            <span className="text-xs text-slate-400">Aniqlik</span>
            <div className="text-3xl font-black text-emerald-400 font-mono mt-1">
              {Math.round((score / (questions.length * 10)) * 100)}%
            </div>
          </div>
        </div>

        <button
          onClick={generateQuestions}
          className="w-full py-4 bg-gradient-to-r from-amber-500 to-orange-500 hover:from-amber-600 hover:to-orange-600 text-slate-950 font-black rounded-2xl shadow-lg shadow-amber-500/20 transition-all flex items-center justify-center gap-2 cursor-pointer"
        >
          <RotateCcw className="w-5 h-5" />
          <span>Qaytadan O&apos;ynash</span>
        </button>
      </div>
    );
  }

  const currentQ = questions[currentIndex];
  const progressPercent = ((currentIndex + 1) / questions.length) * 100;
  const timerPercent = (timeLeft / 15) * 100;

  return (
    <div className="max-w-md mx-auto p-4 space-y-4 pb-12">
      {/* Top Header Bar */}
      <div className="flex items-center justify-between bg-slate-900/60 border border-slate-800/80 rounded-2xl p-3">
        <div className="flex items-center gap-2">
          <Zap className="w-5 h-5 text-amber-400 fill-amber-400 animate-pulse" />
          <span className="text-xs font-bold text-slate-300">Speed Quiz ({langFlag})</span>
          <span className="text-xs text-slate-500">• {currentIndex + 1}/{questions.length}</span>
        </div>
        <div className="flex items-center gap-3">
          {streak > 1 && (
            <span className="px-2 py-0.5 text-xs font-bold bg-amber-500/20 text-amber-400 border border-amber-500/30 rounded-full flex items-center gap-1">
              🔥 {streak}x
            </span>
          )}
          <span className="text-xs font-black text-white bg-slate-800 px-2.5 py-1 rounded-xl">
            {score} ball
          </span>
        </div>
      </div>

      {/* Progress & Countdown */}
      <div className="space-y-1.5">
        <div className="w-full bg-slate-800/80 rounded-full h-1.5 overflow-hidden">
          <div
            className="bg-indigo-500 h-1.5 transition-all duration-300"
            style={{ width: `${progressPercent}%` }}
          />
        </div>
        
        <div className="w-full bg-slate-800/80 rounded-full h-2 overflow-hidden flex">
          <div
            className={`h-2 transition-all duration-1000 ${
              timeLeft <= 4 ? 'bg-rose-500 animate-pulse' : timeLeft <= 8 ? 'bg-amber-400' : 'bg-emerald-400'
            }`}
            style={{ width: `${timerPercent}%` }}
          />
        </div>
      </div>

      {/* Question Card */}
      <div className="bg-slate-900/80 border border-slate-800 rounded-3xl p-6 shadow-xl relative overflow-hidden backdrop-blur-md space-y-4">
        <div className="flex items-center justify-between text-xs text-slate-400">
          <span className="flex items-center gap-1">
            <Clock className={`w-4 h-4 ${timeLeft <= 4 ? 'text-rose-400 animate-bounce' : 'text-slate-400'}`} />
            {timeLeft}s
          </span>
          <span className="font-semibold text-indigo-400 px-2.5 py-0.5 rounded-full bg-indigo-500/10 border border-indigo-500/20">
            {currentQ.mode === 'TARGET_TO_UZBEK' ? `${langFlag} -> 🇺🇿 O'zbekcha` : `🇺🇿 -> ${langFlag} Chet tili`}
          </span>
        </div>

        {/* Word Display & Audio */}
        <div className="text-center py-3 space-y-2 bg-slate-950/70 p-4 rounded-2xl border border-slate-800/80">
          <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider">
            {currentQ.displayTitle}
          </div>
          <div className="flex items-center justify-center gap-2">
            <h3 className="text-2xl sm:text-3xl font-black text-white tracking-wide uppercase">
              {currentQ.displayWord}
            </h3>
            <button
              onClick={() => speakWord(currentQ.word.word)}
              className="p-2 text-indigo-400 hover:text-white bg-indigo-500/10 hover:bg-indigo-500/20 rounded-xl transition-all cursor-pointer"
              title="Talaffuz"
            >
              <Volume2 className="w-5 h-5" />
            </button>
          </div>
          <p className="text-xs text-slate-400 font-mono">{currentQ.displaySub}</p>
        </div>

        {/* Options Grid */}
        <div className="space-y-2.5">
          {currentQ.options.map((option, idx) => {
            const isSelected = selectedOption === option;
            const isCorrect = option === currentQ.correctAnswer;
            
            let btnStyle = "bg-slate-800/70 border-slate-700/60 text-slate-200 hover:bg-slate-800 hover:border-indigo-500/50";
            
            if (isAnswered) {
              if (isCorrect) {
                btnStyle = "bg-emerald-500/20 border-emerald-500 text-emerald-300 font-bold shadow-lg shadow-emerald-500/20";
              } else if (isSelected) {
                btnStyle = "bg-rose-500/20 border-rose-500 text-rose-300 font-bold";
              } else {
                btnStyle = "bg-slate-800/30 border-slate-800/50 text-slate-500 opacity-50";
              }
            }

            return (
              <button
                key={idx}
                onClick={() => handleOptionSelect(option)}
                disabled={isAnswered}
                className={`w-full p-3.5 rounded-2xl border text-left flex items-center justify-between transition-all cursor-pointer select-none ${btnStyle}`}
              >
                <div className="flex items-center gap-3">
                  <span className="w-7 h-7 rounded-xl bg-slate-900/60 border border-slate-700 flex items-center justify-center text-xs font-bold text-slate-400">
                    {String.fromCharCode(65 + idx)}
                  </span>
                  <span className="text-sm font-semibold">{option}</span>
                </div>
                {isAnswered && isCorrect && <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />}
                {isAnswered && isSelected && !isCorrect && <XCircle className="w-5 h-5 text-rose-400 shrink-0" />}
              </button>
            );
          })}
        </div>

        {/* Correct answer auto-advance banner */}
        {isAnswered && selectedOption === currentQ.correctAnswer && (
          <div className="p-3 bg-emerald-950/60 border border-emerald-500/40 rounded-2xl flex items-center justify-between text-xs text-emerald-300 animate-fadeIn">
            <div className="flex items-center gap-2 font-bold">
              <CheckCircle2 className="w-4 h-4 text-emerald-400" />
              <span>To'g'ri! Keyingi savolga o'tilmoqda...</span>
            </div>
            <div className="w-4 h-4 border-2 border-emerald-400 border-t-transparent rounded-full animate-spin shrink-0" />
          </div>
        )}

        {/* Mnemonic Hint & Next Button revealed upon WRONG answer */}
        {isAnswered && selectedOption !== currentQ.correctAnswer && (
          <div className="p-4 bg-gradient-to-b from-rose-950/30 to-slate-900 border border-rose-500/30 rounded-2xl text-xs space-y-3 animate-fadeIn">
            <div className="flex items-center justify-between text-rose-300 font-bold">
              <span className="flex items-center gap-1.5">
                <XCircle className="w-4 h-4 text-rose-400" />
                Noto'g'ri javob!
              </span>
              <span className="text-emerald-400 font-medium">To'g'ri: {currentQ.correctAnswer}</span>
            </div>
            
            <div className="p-2.5 bg-amber-500/10 border border-amber-500/20 rounded-xl space-y-1">
              <div className="flex items-center gap-1.5 text-amber-400 font-bold text-[11px]">
                <Sparkles className="w-3.5 h-3.5" />
                Mnemotexnik eslab qolish kodi:
              </div>
              <p className="text-slate-200 leading-relaxed italic text-[11px]">
                &ldquo;{currentQ.word.mnemonicStory}&rdquo;
              </p>
            </div>

            <button
              onClick={advanceToNext}
              className="w-full py-3 bg-indigo-600 hover:bg-indigo-500 text-white font-bold rounded-xl transition-all flex items-center justify-center gap-2 cursor-pointer shadow-lg shadow-indigo-600/30"
            >
              <span>Keyingi Savol</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        )}

      </div>
    </div>
  );
};
