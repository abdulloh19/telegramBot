'use client';

import React, { useState, useEffect } from 'react';
import { useApp } from '@/context/AppContext';
import {
  Volume2,
  ChevronLeft,
  ChevronRight,
  Star,
  CheckCircle,
  Sparkles,
  Zap,
  Film,
  Quote,
  Flame,
  VolumeX,
  RotateCcw,
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { audioManager } from '@/utils/audio';

export const DailyLessonView: React.FC = () => {
  const {
    targetLanguage,
    selectedLevel,
    activeWords,
    currentWordIndex,
    setCurrentWordIndex,
    currentWord,
    completedWordIds,
    markWordCompleted,
    favorites,
    toggleFavorite,
    speakWord,
    isAudioPlaying,
    setActiveTab,
  } = useApp();

  const [autoPlayAudio, setAutoPlayAudio] = useState<boolean>(true);
  const [showCelebration, setShowCelebration] = useState<boolean>(false);

  const total = activeWords.length;
  const currentIndex = currentWordIndex;
  const progressPercent = total > 0 ? Math.round(((currentIndex + 1) / total) * 100) : 0;

  // Speak word when navigating if autoPlayAudio is enabled
  useEffect(() => {
    if (currentWord && autoPlayAudio) {
      const timer = setTimeout(() => {
        speakWord(currentWord.word);
      }, 250);
      return () => clearTimeout(timer);
    }
  }, [currentWordIndex, currentWord?.id]);

  if (!currentWord || total === 0) {
    return (
      <div className="p-8 text-center text-slate-400 bg-slate-900/50 rounded-2xl border border-slate-800">
        Ushbu daraja uchun hozircha so'zlar mavjud emas.
      </div>
    );
  }

  const isFavorite = favorites.has(currentWord.id);
  const isCompleted = completedWordIds.has(currentWord.id);
  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';

  const handleNext = () => {
    markWordCompleted(currentWord.id);
    if (currentIndex < total - 1) {
      setCurrentWordIndex(currentIndex + 1);
    } else {
      triggerFinishCelebration();
    }
  };

  const handlePrev = () => {
    audioManager.playClickSound();
    if (currentIndex > 0) {
      setCurrentWordIndex(currentIndex - 1);
    }
  };

  const triggerFinishCelebration = () => {
    audioManager.playSuccessSound();
    setShowCelebration(true);
    confetti({
      particleCount: 80,
      spread: 70,
      origin: { y: 0.6 },
      colors: ['#6366f1', '#a855f7', '#ec4899', '#10b981'],
    });
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6 pb-28 animate-fade-in">
      
      {/* Top Header & Progress Bar */}
      <div className="space-y-3">
        <div className="flex items-center justify-between text-sm sm:text-base text-slate-300 flex-wrap gap-2">
          <div className="flex items-center gap-2.5">
            <span className="font-extrabold text-white uppercase tracking-wider text-sm sm:text-base">
              {langFlag} Kunlik Dars
            </span>
            <span className="text-slate-500">•</span>
            <span className="font-mono text-indigo-400 font-black text-base sm:text-lg">
              {currentIndex + 1} / {total}
            </span>
          </div>

          <div className="flex items-center gap-2">
            {/* Reset to 1st word button */}
            {currentIndex > 0 && (
              <button
                onClick={() => {
                  audioManager.playClickSound();
                  setCurrentWordIndex(0);
                }}
                className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs sm:text-sm font-bold bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white border border-slate-700 transition-all cursor-pointer"
                title="1-so'zdan qaytadan boshlash"
              >
                <RotateCcw className="w-3.5 h-3.5 text-indigo-400" />
                <span>1-so'zga qaytish</span>
              </button>
            )}

            {/* Auto-Play Toggle Button */}
            <button
              onClick={() => setAutoPlayAudio(!autoPlayAudio)}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs sm:text-sm font-bold transition-all cursor-pointer ${
                autoPlayAudio
                  ? 'bg-indigo-500/20 text-indigo-300 border border-indigo-500/30'
                  : 'bg-slate-800/80 text-slate-400 border border-slate-700'
              }`}
            >
              {autoPlayAudio ? <Volume2 className="w-4 h-4 text-emerald-400" /> : <VolumeX className="w-4 h-4" />}
              <span>Avto-audio: {autoPlayAudio ? 'Yoqiq' : 'O\'chiq'}</span>
            </button>
          </div>
        </div>

        {/* Dynamic Glowing Progress Bar */}
        <div className="w-full h-2.5 rounded-full bg-slate-800 overflow-hidden relative">
          <div
            className="h-full bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 rounded-full transition-all duration-500 shadow-md shadow-indigo-500/50"
            style={{ width: `${progressPercent}%` }}
          />
        </div>
      </div>

      {/* Main 4-Step Interactive Card */}
      <div className="relative rounded-3xl bg-gradient-to-b from-slate-900/90 to-slate-950/95 border border-slate-700/80 p-6 sm:p-8 shadow-2xl backdrop-blur-xl overflow-hidden space-y-6">
        
        {/* Card Header: Word & Audio Action */}
        <div className="flex items-start justify-between gap-4 pb-5 border-b border-slate-800/80">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-indigo-400 uppercase tracking-widest">
                {targetLanguage === 'ru' ? 'СЛОВО (SO\'Z)' : 'WORD (SO\'Z)'}
              </span>
              <span className="text-[11px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 font-mono">
                {selectedLevel}
              </span>
            </div>

            <div className="flex items-baseline gap-3 flex-wrap">
              <h2 className="text-3xl sm:text-4xl font-black text-white uppercase tracking-tight">
                {currentWord.word}
              </h2>
              <span className="text-base font-mono text-slate-400 font-medium">
                {currentWord.pronunciation}
              </span>
            </div>

            <div className="text-xl sm:text-2xl font-bold text-emerald-400 pt-1">
              🇺🇿 {currentWord.uzbekMeaning}
            </div>
          </div>

          {/* Right Actions: Favorite & Large Audio Trigger */}
          <div className="flex items-center gap-2">
            <button
              onClick={() => toggleFavorite(currentWord.id)}
              className={`p-2.5 rounded-xl border transition-all cursor-pointer ${
                isFavorite
                  ? 'bg-amber-500/20 text-amber-400 border-amber-500/40'
                  : 'bg-slate-800/50 text-slate-400 border-slate-700 hover:text-slate-200'
              }`}
            >
              <Star className="w-5 h-5 fill-current" />
            </button>

            <button
              onClick={() => speakWord(currentWord.word)}
              className={`p-3 rounded-2xl shadow-xl transition-all cursor-pointer group flex items-center gap-2 ${
                isAudioPlaying
                  ? 'bg-emerald-500 text-white animate-bounce'
                  : 'bg-gradient-to-tr from-indigo-600 to-purple-600 text-white hover:scale-105 hover:shadow-indigo-500/40'
              }`}
            >
              <Volume2 className="w-6 h-6 group-hover:scale-110 transition-transform" />
            </button>
          </div>
        </div>

        {/* 4 Creative Steps Grid */}
        <div className="space-y-4">
          
          {/* Step 1: Phonetic Hook */}
          <div className="p-4 rounded-2xl bg-amber-500/10 border border-amber-500/25 space-y-1.5">
            <div className="flex items-center gap-2 text-xs font-bold text-amber-400 uppercase tracking-wider">
              <Zap className="w-4 h-4" />
              <span>1. Fonetik Ilmoq (Eshitish assotsiatsiyasi):</span>
            </div>
            <p className="text-sm font-semibold text-amber-200 pl-6">
              👉 «{currentWord.mnemonicHook}»
            </p>
          </div>

          {/* Step 2: Kinematic Image / Story */}
          <div className="p-4 rounded-2xl bg-purple-500/10 border border-purple-500/25 space-y-1.5">
            <div className="flex items-center gap-2 text-xs font-bold text-purple-400 uppercase tracking-wider">
              <Film className="w-4 h-4" />
              <span>2. Kinematik Obraz (Jonli tasavvur qiling):</span>
            </div>
            <p className="text-sm text-slate-200 leading-relaxed pl-6">
              {currentWord.mnemonicStory}
            </p>
          </div>

          {/* Step 3: Context Example */}
          <div className="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 space-y-2">
            <div className="flex items-center gap-2 text-xs font-bold text-indigo-400 uppercase tracking-wider">
              <Quote className="w-4 h-4" />
              <span>3. Kontekst & Misol Gap:</span>
            </div>
            <div className="pl-6 space-y-1 text-sm">
              <p className="text-white font-medium italic">
                {langFlag} "{currentWord.exampleTarget}"
              </p>
              <p className="text-slate-400 text-xs italic">
                🇺🇿 "{currentWord.exampleUz}"
              </p>
            </div>
          </div>

          {/* Step 4: Active Memory Activation */}
          <div className="p-3.5 rounded-xl bg-slate-900/60 border border-slate-800/80 text-xs text-slate-300 flex items-center gap-3">
            <span className="text-base">⚡</span>
            <span>
              <b>3 soniyalik qoida:</b> Ko'zingizni yumib, voqeani tasavvur qiling va so'zni ovoz chiqarib qaytaring!
            </span>
          </div>

        </div>

        {/* Navigation Action Buttons */}
        <div className="flex items-center justify-between gap-3 pt-4 border-t border-slate-800">
          
          <button
            onClick={handlePrev}
            disabled={currentIndex === 0}
            className={`flex items-center gap-1.5 px-4 py-2.5 rounded-xl text-xs font-bold border transition-all cursor-pointer ${
              currentIndex === 0
                ? 'opacity-40 pointer-events-none border-slate-800 text-slate-600'
                : 'border-slate-700 text-slate-300 hover:bg-slate-800 hover:text-white'
            }`}
          >
            <ChevronLeft className="w-4 h-4" />
            <span>Oldingi</span>
          </button>

          {/* Next / Finish Button */}
          <button
            onClick={handleNext}
            className="flex-1 max-w-xs flex items-center justify-center gap-2 px-6 py-3 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-bold text-sm shadow-lg shadow-indigo-500/25 hover:shadow-indigo-500/40 hover:scale-[1.02] active:scale-[0.98] transition-all cursor-pointer select-none"
          >
            {currentIndex < total - 1 ? (
              <>
                <span>Keyingi so'z ({currentIndex + 2}/{total})</span>
                <ChevronRight className="w-4 h-4" />
              </>
            ) : (
              <>
                <CheckCircle className="w-4 h-4 text-emerald-300" />
                <span>Darsni yakunlash</span>
              </>
            )}
          </button>

        </div>

      </div>

      {/* Celebration Modal when Lesson is finished */}
      {showCelebration && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fade-in">
          <div className="max-w-md w-full p-6 sm:p-8 rounded-3xl bg-slate-900 border border-indigo-500/40 shadow-2xl text-center space-y-5">
            <div className="w-16 h-16 rounded-3xl bg-gradient-to-tr from-amber-500 to-orange-500 flex items-center justify-center mx-auto text-white shadow-xl shadow-amber-500/30 animate-bounce">
              <Sparkles className="w-8 h-8" />
            </div>

            <div className="space-y-2">
              <h3 className="text-2xl font-black text-white">
                Tabriklaymiz! 🎓
              </h3>
              <p className="text-sm text-slate-300">
                Bugungi barcha <b>20 ta mnemonik so'zlarni</b> muvaffaqiyatli o'rgandingiz!
              </p>
            </div>

            <div className="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 flex justify-around">
              <div>
                <div className="text-xl font-bold text-amber-400 font-mono">+{total}</div>
                <div className="text-[10px] text-slate-400">Yangi so'z</div>
              </div>
              <div>
                <div className="text-xl font-bold text-emerald-400 font-mono">100%</div>
                <div className="text-[10px] text-slate-400">O'zlashtirish</div>
              </div>
              <div>
                <div className="text-xl font-bold text-purple-400 font-mono">+50p</div>
                <div className="text-[10px] text-slate-400">Bonus ochko</div>
              </div>
            </div>

            <div className="flex flex-col gap-2.5">
              <button
                onClick={() => {
                  setShowCelebration(false);
                  setCurrentWordIndex(0);
                }}
                className="w-full py-3 rounded-xl bg-slate-800 text-slate-200 font-bold text-sm hover:bg-slate-700 transition-all cursor-pointer"
              >
                🔄 1-so'zdan qaytadan o'tish
              </button>

              <button
                onClick={() => {
                  setShowCelebration(false);
                  setActiveTab('dialogue');
                }}
                className="w-full py-3 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 text-white font-bold text-sm shadow-lg shadow-emerald-500/25 hover:scale-[1.02] transition-all cursor-pointer"
              >
                🗣️ So'zlardan tuzilgan Dialog & Audioni eshitish
              </button>

              <button
                onClick={() => {
                  setShowCelebration(false);
                  setActiveTab('practice');
                }}
                className="w-full py-3 rounded-xl bg-slate-800 text-slate-200 font-semibold text-sm hover:bg-slate-700 transition-all cursor-pointer"
              >
                📝 5 ta Test orqali bilimlarni sinash
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};
