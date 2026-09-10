'use client';

import React, { useState, useMemo } from 'react';
import { useApp } from '@/context/AppContext';
import { TargetLanguage, WordLevel, MnemonicWord } from '@/types';
import { MNEMONIC_WORDS } from '@/data/words';
import { DAILY_DIALOGUES } from '@/data/dialogues';
import {
  BookMarked,
  Sparkles,
  Volume2,
  VolumeX,
  Search,
  CheckCircle2,
  Circle,
  Heart,
  Flame,
  Trophy,
  Filter,
  Layers,
  ArrowRight,
  MessageSquareQuote,
  Zap,
  TrendingUp,
  BrainCircuit,
  RotateCcw,
} from 'lucide-react';

export const LearnedWordsView: React.FC = () => {
  const {
    targetLanguage,
    setTargetLanguage,
    stats,
    completedWordIds,
    toggleWordCompleted,
    favorites,
    toggleFavorite,
    completedDialogueIds,
    setSelectedDialogueId,
    setActiveTab,
    speakWord,
    isAudioPlaying,
  } = useApp();

  // Active view language (synced with targetLanguage, but user can explicitly switch here)
  const [selectedLang, setSelectedLang] = useState<TargetLanguage>(targetLanguage);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'COMPLETED' | 'LEARNING' | 'FAVORITES'>('ALL');
  const [levelFilter, setLevelFilter] = useState<WordLevel | 'ALL'>('ALL');
  const [playingWordId, setPlayingWordId] = useState<string | null>(null);

  // Sync when parent targetLanguage changes
  const handleSwitchLanguage = (lang: TargetLanguage) => {
    setSelectedLang(lang);
    setTargetLanguage(lang);
  };

  // 1. Strictly isolated words for the selected language
  const wordsForLang = useMemo(() => {
    return MNEMONIC_WORDS.filter(w => w.language === selectedLang);
  }, [selectedLang]);

  // 2. Strictly isolated dialogues for the selected language
  const dialoguesForLang = useMemo(() => {
    return DAILY_DIALOGUES.filter(d => d.language === selectedLang);
  }, [selectedLang]);

  // Statistics for selected language
  const langCompletedWords = useMemo(() => {
    return wordsForLang.filter(w => completedWordIds.has(w.id));
  }, [wordsForLang, completedWordIds]);

  const langLearningWords = useMemo(() => {
    return wordsForLang.filter(w => !completedWordIds.has(w.id));
  }, [wordsForLang, completedWordIds]);

  const langFavoriteWords = useMemo(() => {
    return wordsForLang.filter(w => favorites.has(w.id));
  }, [wordsForLang, favorites]);

  const langCompletedDialogues = useMemo(() => {
    return dialoguesForLang.filter(d => completedDialogueIds.has(d.id));
  }, [dialoguesForLang, completedDialogueIds]);

  const progressPercentage = wordsForLang.length > 0
    ? Math.round((langCompletedWords.length / wordsForLang.length) * 100)
    : 0;

  // Filtered list based on search, status, and level
  const filteredWords = useMemo(() => {
    return wordsForLang.filter(w => {
      // Status filter
      if (statusFilter === 'COMPLETED' && !completedWordIds.has(w.id)) return false;
      if (statusFilter === 'LEARNING' && completedWordIds.has(w.id)) return false;
      if (statusFilter === 'FAVORITES' && !favorites.has(w.id)) return false;

      // Level filter
      if (levelFilter !== 'ALL' && w.level !== levelFilter) return false;

      // Search query filter
      if (searchQuery.trim() !== '') {
        const query = searchQuery.toLowerCase();
        const matchesWord = w.word.toLowerCase().includes(query);
        const matchesUz = w.uzbekMeaning.toLowerCase().includes(query);
        const matchesHook = w.mnemonicHook.toLowerCase().includes(query);
        const matchesStory = w.mnemonicStory.toLowerCase().includes(query);
        const matchesPron = w.pronunciation.toLowerCase().includes(query);
        return matchesWord || matchesUz || matchesHook || matchesStory || matchesPron;
      }

      return true;
    });
  }, [wordsForLang, statusFilter, levelFilter, searchQuery, completedWordIds, favorites]);

  const handlePlayWord = (word: MnemonicWord) => {
    setPlayingWordId(word.id);
    speakWord(word.word, () => {
      setPlayingWordId(null);
    });
  };

  const handleGoToDialogue = (dialogueId: string) => {
    setSelectedDialogueId(dialogueId);
    setActiveTab('dialogue');
  };

  return (
    <div className="space-y-6 pb-16 animate-fadeIn">
      
      {/* 1. LANGUAGE SELECTOR HEADER (Strict Separation) */}
      <div className="p-4 sm:p-5 rounded-3xl bg-slate-900/90 border border-slate-800 shadow-xl space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <span className="p-2 rounded-xl bg-indigo-500/15 text-indigo-400">
                <BookMarked className="w-5 h-5" />
              </span>
              <h2 className="text-xl sm:text-2xl font-black text-white tracking-tight">
                Mening Lug'atim & O'zlashtirilgan So'zlar
              </h2>
            </div>
            <p className="text-xs sm:text-sm text-slate-400">
              Rus va ingliz tillari bo'yicha mustaqil statistika, mnemonik kartalar va takrorlash
            </p>
          </div>

          <div className="flex items-center gap-1 px-3 py-1.5 rounded-2xl bg-amber-500/10 border border-amber-500/25 text-amber-400 font-mono text-xs font-bold self-start sm:self-auto">
            <Flame className="w-4 h-4" />
            <span>Streak: {stats.streak} kun</span>
          </div>
        </div>

        {/* Big Dual-Language Switcher Tabs */}
        <div className="grid grid-cols-2 gap-2.5 p-1.5 rounded-2xl bg-slate-950/80 border border-slate-800">
          
          {/* Russian Tab */}
          <button
            onClick={() => handleSwitchLanguage('ru')}
            className={`p-3 rounded-xl flex items-center justify-between transition-all cursor-pointer ${
              selectedLang === 'ru'
                ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg shadow-blue-500/25'
                : 'text-slate-400 hover:text-slate-200 hover:bg-slate-900'
            }`}
          >
            <div className="flex items-center gap-2.5">
              <span className="text-2xl">🇷🇺</span>
              <div className="text-left">
                <div className="text-xs sm:text-sm font-black tracking-wide">
                  Rus Tili Lug'ati
                </div>
                <div className={`text-[11px] font-medium ${selectedLang === 'ru' ? 'text-blue-100' : 'text-slate-500'}`}>
                  {MNEMONIC_WORDS.filter(w => w.language === 'ru' && completedWordIds.has(w.id)).length} ta yodlangan
                </div>
              </div>
            </div>

            <span className={`text-[11px] px-2 py-0.5 rounded-full font-bold uppercase ${
              selectedLang === 'ru'
                ? 'bg-white/20 text-white'
                : 'bg-slate-800 text-slate-400'
            }`}>
              {Math.round((MNEMONIC_WORDS.filter(w => w.language === 'ru' && completedWordIds.has(w.id)).length / Math.max(1, MNEMONIC_WORDS.filter(w => w.language === 'ru').length)) * 100)}%
            </span>
          </button>

          {/* English Tab */}
          <button
            onClick={() => handleSwitchLanguage('en')}
            className={`p-3 rounded-xl flex items-center justify-between transition-all cursor-pointer ${
              selectedLang === 'en'
                ? 'bg-gradient-to-r from-indigo-600 to-violet-600 text-white shadow-lg shadow-indigo-500/25'
                : 'text-slate-400 hover:text-slate-200 hover:bg-slate-900'
            }`}
          >
            <div className="flex items-center gap-2.5">
              <span className="text-2xl">🇬🇧</span>
              <div className="text-left">
                <div className="text-xs sm:text-sm font-black tracking-wide">
                  Ingliz Tili Lug'ati
                </div>
                <div className={`text-[11px] font-medium ${selectedLang === 'en' ? 'text-indigo-100' : 'text-slate-500'}`}>
                  {MNEMONIC_WORDS.filter(w => w.language === 'en' && completedWordIds.has(w.id)).length} ta yodlangan
                </div>
              </div>
            </div>

            <span className={`text-[11px] px-2 py-0.5 rounded-full font-bold uppercase ${
              selectedLang === 'en'
                ? 'bg-white/20 text-white'
                : 'bg-slate-800 text-slate-400'
            }`}>
              {Math.round((MNEMONIC_WORDS.filter(w => w.language === 'en' && completedWordIds.has(w.id)).length / Math.max(1, MNEMONIC_WORDS.filter(w => w.language === 'en').length)) * 100)}%
            </span>
          </button>

        </div>
      </div>

      {/* 2. DEDICATED LANGUAGE KPI METRICS CARDS */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        {/* Metric 1: Words Mastered */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-md flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-bold">O'zlashtirilgan</span>
            <div className={`p-1.5 rounded-xl ${selectedLang === 'ru' ? 'bg-blue-500/15 text-blue-400' : 'bg-indigo-500/15 text-indigo-400'}`}>
              <CheckCircle2 className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2">
            <div className="text-2xl font-black text-white font-mono">
              {langCompletedWords.length}{' '}
              <span className="text-xs font-bold text-slate-400">/ {wordsForLang.length} ta</span>
            </div>
            <div className="w-full bg-slate-800 h-1.5 rounded-full mt-2 overflow-hidden">
              <div
                className={`h-full rounded-full transition-all duration-500 ${
                  selectedLang === 'ru'
                    ? 'bg-gradient-to-r from-blue-500 to-cyan-400'
                    : 'bg-gradient-to-r from-indigo-500 to-purple-500'
                }`}
                style={{ width: `${progressPercentage}%` }}
              />
            </div>
          </div>
        </div>

        {/* Metric 2: Dialogues Mastered */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-md flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-bold">Dialoglar</span>
            <div className="p-1.5 rounded-xl bg-emerald-500/15 text-emerald-400">
              <MessageSquareQuote className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2">
            <div className="text-2xl font-black text-white font-mono">
              {langCompletedDialogues.length}{' '}
              <span className="text-xs font-bold text-slate-400">/ {dialoguesForLang.length} ta</span>
            </div>
            <div className="text-[11px] text-emerald-400 font-medium mt-1">
              5 mavzu + 1 combo
            </div>
          </div>
        </div>

        {/* Metric 3: Favorites */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-md flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-bold">Sevimlilar</span>
            <div className="p-1.5 rounded-xl bg-rose-500/15 text-rose-400">
              <Heart className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2">
            <div className="text-2xl font-black text-white font-mono">
              {langFavoriteWords.length}{' '}
              <span className="text-xs font-bold text-slate-400">ta</span>
            </div>
            <div className="text-[11px] text-rose-400 font-medium mt-1">
              Qayta takrorlash uchun
            </div>
          </div>
        </div>

        {/* Metric 4: Neuro-Retention LTM */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-md flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-bold">Neyro-Xotira</span>
            <div className="p-1.5 rounded-xl bg-amber-500/15 text-amber-400">
              <BrainCircuit className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2">
            <div className="text-2xl font-black text-white font-mono">
              {progressPercentage}%
            </div>
            <div className="text-[11px] text-amber-300 font-medium mt-1 flex items-center gap-1">
              <Zap className="w-3 h-3" />
              <span>LTM Xotira bazasi</span>
            </div>
          </div>
        </div>
      </div>

      {/* 3. DIALOGUES STATUS OVERVIEW (Per Language) */}
      <div className="p-4 sm:p-5 rounded-3xl bg-slate-900/80 border border-slate-800 shadow-lg space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <MessageSquareQuote className="w-4 h-4 text-emerald-400" />
            <h3 className="text-sm sm:text-base font-black text-white">
              {selectedLang === 'ru' ? '🇷🇺 Ruscha' : '🇬🇧 Inglizcha'} Jonli Dialoglar Holati (5 Mavzu + Combo)
            </h3>
          </div>
          <span className="text-xs font-bold text-slate-400">
            {langCompletedDialogues.length}/{dialoguesForLang.length} yakunlandi
          </span>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
          {dialoguesForLang.map((dialogue) => {
            const isCompleted = completedDialogueIds.has(dialogue.id);

            return (
              <div
                key={dialogue.id}
                onClick={() => handleGoToDialogue(dialogue.id)}
                className={`p-3 rounded-2xl border transition-all cursor-pointer flex flex-col justify-between ${
                  isCompleted
                    ? 'bg-emerald-950/20 border-emerald-500/40 hover:bg-emerald-950/30'
                    : 'bg-slate-950/60 border-slate-800 hover:border-slate-700'
                }`}
              >
                <div className="flex items-start justify-between gap-1">
                  <div className="space-y-0.5">
                    <span className="text-[10px] font-extrabold uppercase px-1.5 py-0.5 rounded-full bg-slate-800 text-slate-300">
                      {dialogue.category || 'Mavzu'}
                    </span>
                    <h4 className="text-xs font-bold text-white line-clamp-1 mt-1">
                      {dialogue.title}
                    </h4>
                  </div>
                  {isCompleted ? (
                    <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
                  ) : (
                    <Circle className="w-4 h-4 text-slate-600 shrink-0" />
                  )}
                </div>

                <div className="mt-2.5 flex items-center justify-between text-[11px]">
                  <span className="text-slate-400">{dialogue.lines.length} ta replika</span>
                  <span className={`font-bold flex items-center gap-0.5 ${isCompleted ? 'text-emerald-400' : 'text-indigo-400'}`}>
                    <span>{isCompleted ? 'Takrorlash' : 'Boshlash'}</span>
                    <ArrowRight className="w-3 h-3" />
                  </span>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* 4. SEARCH & ADVANCED FILTER CONTROLS */}
      <div className="space-y-3">
        {/* Search Input */}
        <div className="relative">
          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder={`${selectedLang === 'ru' ? 'Ruscha so\'z, o\'zbekcha ma\'no yoki mnemonik ilmoq qidiring...' : 'Inglizcha so\'z, o\'zbekcha ma\'no yoki ilmoq qidiring...'}`}
            className="w-full pl-10 pr-4 py-3 rounded-2xl bg-slate-900 border border-slate-800 text-white placeholder-slate-500 text-xs sm:text-sm font-medium focus:outline-none focus:border-indigo-500 transition-colors"
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-slate-400 hover:text-white px-1.5 py-0.5 rounded-lg bg-slate-800"
            >
              Tozalash
            </button>
          )}
        </div>

        {/* Status Filter Tabs */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 scrollbar-none text-xs font-bold">
          <button
            onClick={() => setStatusFilter('ALL')}
            className={`px-3 py-1.5 rounded-xl transition-all shrink-0 cursor-pointer ${
              statusFilter === 'ALL'
                ? 'bg-white text-slate-950 shadow-md'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-white'
            }`}
          >
            Barchasi ({wordsForLang.length})
          </button>

          <button
            onClick={() => setStatusFilter('COMPLETED')}
            className={`px-3 py-1.5 rounded-xl transition-all shrink-0 cursor-pointer flex items-center gap-1 ${
              statusFilter === 'COMPLETED'
                ? 'bg-emerald-500 text-white shadow-md shadow-emerald-500/20'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-emerald-400'
            }`}
          >
            <CheckCircle2 className="w-3.5 h-3.5" />
            <span>Yodlangan ({langCompletedWords.length})</span>
          </button>

          <button
            onClick={() => setStatusFilter('LEARNING')}
            className={`px-3 py-1.5 rounded-xl transition-all shrink-0 cursor-pointer flex items-center gap-1 ${
              statusFilter === 'LEARNING'
                ? 'bg-amber-500 text-white shadow-md shadow-amber-500/20'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-amber-400'
            }`}
          >
            <Circle className="w-3.5 h-3.5" />
            <span>O'rganilmoqda ({langLearningWords.length})</span>
          </button>

          <button
            onClick={() => setStatusFilter('FAVORITES')}
            className={`px-3 py-1.5 rounded-xl transition-all shrink-0 cursor-pointer flex items-center gap-1 ${
              statusFilter === 'FAVORITES'
                ? 'bg-rose-500 text-white shadow-md shadow-rose-500/20'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-rose-400'
            }`}
          >
            <Heart className="w-3.5 h-3.5" />
            <span>Sevimlilar ({langFavoriteWords.length})</span>
          </button>
        </div>

        {/* Level Filter Pills */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 text-xs">
          <span className="text-[11px] font-bold text-slate-500 mr-1 flex items-center gap-1">
            <Filter className="w-3 h-3" />
            <span>Daraja:</span>
          </span>

          {(['ALL', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'] as const).map((lvl) => {
            const label =
              lvl === 'ALL'
                ? 'Hammasi'
                : lvl === 'BEGINNER'
                ? '🟢 A1-A2 Boshlang\'ich'
                : lvl === 'INTERMEDIATE'
                ? '🟡 B1-B2 O\'rta'
                : '🔴 C1-C2 Yuqori';

            return (
              <button
                key={lvl}
                onClick={() => setLevelFilter(lvl)}
                className={`px-2.5 py-1 rounded-lg font-bold transition-all shrink-0 cursor-pointer ${
                  levelFilter === lvl
                    ? 'bg-indigo-600 text-white'
                    : 'bg-slate-900/80 border border-slate-800 text-slate-400 hover:text-slate-200'
                }`}
              >
                {label}
              </button>
            );
          })}
        </div>
      </div>

      {/* 5. INTERACTIVE WORDS LIST */}
      <div className="space-y-3">
        <div className="flex items-center justify-between text-xs text-slate-400 px-1">
          <span>Topilgan so'zlar: <strong className="text-white">{filteredWords.length}</strong> ta</span>
          <span>Til: <strong className="text-indigo-400 uppercase">{selectedLang}</strong></span>
        </div>

        {filteredWords.length === 0 ? (
          <div className="p-8 rounded-3xl bg-slate-900/60 border border-slate-800 text-center space-y-3">
            <div className="w-12 h-12 rounded-2xl bg-slate-800 text-slate-400 flex items-center justify-center mx-auto">
              <Search className="w-6 h-6" />
            </div>
            <div>
              <h4 className="text-base font-bold text-white">Hech qanday so'z topilmadi</h4>
              <p className="text-xs text-slate-400 mt-1">
                Qidiruv so'zini o'zgartiring yoki filtrlarni qayta sozlang.
              </p>
            </div>
            <button
              onClick={() => {
                setSearchQuery('');
                setStatusFilter('ALL');
                setLevelFilter('ALL');
              }}
              className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold transition-all cursor-pointer inline-flex items-center gap-1.5"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              <span>Filtrlarni tozalash</span>
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-3">
            {filteredWords.map((word) => {
              const isCompleted = completedWordIds.has(word.id);
              const isFav = favorites.has(word.id);
              const isPlaying = isAudioPlaying && playingWordId === word.id;

              return (
                <div
                  key={word.id}
                  className={`p-4 sm:p-5 rounded-2xl border transition-all ${
                    isCompleted
                      ? 'bg-slate-900/80 border-emerald-500/30 hover:border-emerald-500/50'
                      : 'bg-slate-900/70 border-slate-800 hover:border-indigo-500/40'
                  }`}
                >
                  {/* Card Header: Word, Pronunciation, Sound, Badges */}
                  <div className="flex items-start justify-between gap-3">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2.5 flex-wrap">
                        <span className="text-xl sm:text-2xl font-black text-white tracking-tight">
                          {word.word}
                        </span>

                        <button
                          onClick={() => handlePlayWord(word)}
                          title="Talaffuzni tinglash"
                          className={`p-1.5 rounded-xl border transition-all cursor-pointer ${
                            isPlaying
                              ? 'bg-emerald-500 text-white border-emerald-400 animate-pulse'
                              : 'bg-slate-800 border-slate-700 text-slate-300 hover:text-white hover:bg-slate-700'
                          }`}
                        >
                          <Volume2 className="w-4 h-4" />
                        </button>

                        <span className="text-xs font-mono text-slate-400 bg-slate-950 px-2 py-0.5 rounded-lg border border-slate-800">
                          {word.pronunciation}
                        </span>

                        <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-md bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                          {word.level}
                        </span>
                      </div>

                      {/* Uzbek Translation */}
                      <div className="text-sm sm:text-base font-bold text-amber-300 flex items-center gap-1.5 pt-0.5">
                        <span>🇺🇿</span>
                        <span>{word.uzbekMeaning}</span>
                      </div>
                    </div>

                    {/* Actions: Favorite & Mastered Buttons */}
                    <div className="flex items-center gap-1.5 shrink-0">
                      <button
                        onClick={() => toggleFavorite(word.id)}
                        className={`p-2 rounded-xl border transition-all cursor-pointer ${
                          isFav
                            ? 'bg-rose-500/20 border-rose-500/50 text-rose-400'
                            : 'bg-slate-800/80 border-slate-700 text-slate-400 hover:text-white'
                        }`}
                        title={isFav ? "Sevimlilardan o'chirish" : "Sevimlilarga qo'shish"}
                      >
                        <Heart className={`w-4 h-4 ${isFav ? 'fill-rose-400' : ''}`} />
                      </button>

                      <button
                        onClick={() => toggleWordCompleted(word.id)}
                        className={`px-3 py-1.5 rounded-xl font-bold text-xs border transition-all cursor-pointer flex items-center gap-1.5 ${
                          isCompleted
                            ? 'bg-emerald-500/20 border-emerald-500/50 text-emerald-300'
                            : 'bg-slate-800/80 border-slate-700 text-slate-300 hover:text-white hover:border-emerald-500/40'
                        }`}
                      >
                        <CheckCircle2 className={`w-4 h-4 ${isCompleted ? 'text-emerald-400' : 'text-slate-500'}`} />
                        <span className="hidden sm:inline">{isCompleted ? 'Yodlandi' : 'Yodlangan deb belgilash'}</span>
                      </button>
                    </div>
                  </div>

                  {/* Mnemonics Hook & Story */}
                  <div className="mt-3.5 p-3 rounded-xl bg-slate-950/80 border border-slate-800 space-y-1.5">
                    <div className="flex items-center gap-1.5 text-xs font-bold text-amber-400">
                      <Zap className="w-3.5 h-3.5" />
                      <span>Fonetik Ilmoq: «{word.mnemonicHook}»</span>
                    </div>
                    <p className="text-xs sm:text-sm text-slate-300 italic leading-relaxed">
                      "{word.mnemonicStory}"
                    </p>
                  </div>

                  {/* Real-life Context Sentence */}
                  {word.exampleTarget && (
                    <div className="mt-2.5 pt-2.5 border-t border-slate-800/80 text-xs space-y-1">
                      <div className="text-slate-300 font-medium">
                        💬 <span className="font-semibold text-white">{word.exampleTarget}</span>
                      </div>
                      {word.exampleUz && (
                        <div className="text-slate-400 pl-4">
                          ↳ {word.exampleUz}
                        </div>
                      )}
                    </div>
                  )}

                  {/* Card Footer: Quick Jump to Flashcard Practice */}
                  <div className="mt-3 flex items-center justify-between text-[11px] text-slate-400 pt-1">
                    <span className="flex items-center gap-1">
                      <Sparkles className="w-3 h-3 text-indigo-400" />
                      <span>LTM Neyro-xotira indeksi</span>
                    </span>

                    <button
                      onClick={() => setActiveTab('games')}
                      className="text-indigo-400 hover:text-indigo-300 font-bold flex items-center gap-1 cursor-pointer transition-colors"
                    >
                      <span>3D Flashkartada mashq qilish</span>
                      <ArrowRight className="w-3 h-3" />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      {/* 6. NEURO-LEARNING FOOTER BANNER */}
      <div className="p-4 sm:p-5 rounded-2xl bg-gradient-to-r from-slate-900 via-indigo-950/40 to-slate-900 border border-indigo-500/25 flex items-start gap-3.5">
        <div className="p-2.5 rounded-xl bg-indigo-500/20 text-indigo-300 shrink-0">
          <BrainCircuit className="w-5 h-5" />
        </div>
        <div className="space-y-1 text-xs sm:text-sm">
          <h4 className="font-bold text-white flex items-center gap-1.5">
            <span>Til ajratish va intervalli takrorlash qoidasi</span>
          </h4>
          <p className="text-slate-300 leading-relaxed font-medium">
            Miyangiz so'zlarni chalkashtirmasligi uchun Rus va Ingliz tili lug'atlari mutlaqo alohida saqlanadi. Har bir til bo'yicha yodlangan so'zlarni kamida haftasiga bir marta <b>3D Flashkarta</b> yoki <b>Speed Quiz</b> orqali qayta sinab turing.
          </p>
        </div>
      </div>

    </div>
  );
};
