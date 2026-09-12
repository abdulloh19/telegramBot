'use client';

import React, { useState } from 'react';
import { useApp } from '@/context/AppContext';
import { TargetLanguage, WordLevel, AppTab } from '@/types';
import {
  Flame,
  Sparkles,
  Volume2,
  Menu,
  X,
  Search,
  LayoutDashboard,
  Gamepad2,
  BookOpen,
  MessageSquareQuote,
  CheckCircle2,
  Trophy,
  ArrowRight,
  BookMarked,
} from 'lucide-react';
import { audioManager } from '@/utils/audio';

export const Header: React.FC = () => {
  const {
    targetLanguage,
    setTargetLanguage,
    selectedLevel,
    setSelectedLevel,
    activeTab,
    setActiveTab,
    stats,
    speakWord,
    completedWordsRu,
    completedWordsEn,
    activeWords,
  } = useApp();

  const [isSuperMenuOpen, setIsSuperMenuOpen] = useState<boolean>(false);

  const handleTestAudio = () => {
    try {
      audioManager.playClickSound();
    } catch {}
    const testPhrase = targetLanguage === 'ru' ? 'Здравствуйте! Добро пожаловать!' : 'Hello! Welcome to Mnemonic learning!';
    speakWord(testPhrase);
  };

  const handleSelectTab = (tab: AppTab) => {
    try {
      audioManager.playClickSound();
    } catch {}
    setActiveTab(tab);
    setIsSuperMenuOpen(false);
  };

  const menuItems = [
    {
      id: 'dashboard' as AppTab,
      title: 'Asosiy Boshqaruv',
      subtitle: 'Dashboard, foydalanuvchilar tahlili va kun so\'zi',
      icon: LayoutDashboard,
      color: 'from-blue-500 to-indigo-600',
    },
    {
      id: 'lesson' as AppTab,
      title: `${activeWords.length} ta Kun So'zi`,
      subtitle: 'Mnemonik assotsiatsiya va obrazlar bilan 4-bosqich',
      icon: Sparkles,
      badge: `${activeWords.length} ta`,
      color: 'from-indigo-500 to-purple-600',
    },
    {
      id: 'games' as AppTab,
      title: 'Xotira O\'yinlari Hub',
      subtitle: 'Flashkarta, Juftlikni top, So\'z yig\'ish va Saralash',
      icon: Gamepad2,
      badge: 'XP',
      color: 'from-emerald-500 to-teal-600',
    },
    {
      id: 'grammar' as AppTab,
      title: 'Grammatika Bo\'limi',
      subtitle: 'Rus va ingliz tili qoidalari, misollar va testlar',
      icon: BookOpen,
      color: 'from-pink-500 to-rose-600',
    },
    {
      id: 'dialogue' as AppTab,
      title: 'Jonli Dialog & Speaking',
      subtitle: 'Supermarket, Taksi, Kafe va kundalik suhbatlar',
      icon: MessageSquareQuote,
      badge: 'Audio',
      color: 'from-cyan-500 to-blue-600',
    },
    {
      id: 'practice' as AppTab,
      title: 'Mustahkamlovchi Mashqlar',
      subtitle: '5 ta interaktiv test va mnemonik tushuntirish',
      icon: CheckCircle2,
      badge: '5 ta',
      color: 'from-violet-500 to-fuchsia-600',
    },
    {
      id: 'quiz' as AppTab,
      title: 'Tezkor Viktorina (Speed Quiz)',
      subtitle: '15 soniyalik chaqmoq test va reyting ballari',
      icon: Trophy,
      badge: `${stats.quizScores}p`,
      color: 'from-amber-500 to-orange-600',
    },
    {
      id: 'vocabulary' as AppTab,
      title: 'Mening Lug\'atim (Rus & Ingliz)',
      subtitle: 'Yodlangan so\'zlar, statistika va alohida til bo\'limlari',
      icon: BookMarked,
      badge: `${stats.wordsLearnedCount} ta`,
      color: 'from-blue-500 to-indigo-600',
    },
    {
      id: 'search' as AppTab,
      title: 'Mnemotexnik Qidiruv',
      subtitle: 'Istalgan so\'zni tarjimasi yoki talaffuzi bilan qidiring',
      icon: Search,
      color: 'from-sky-500 to-cyan-600',
    },
  ];

  return (
    <>
      <header className="sticky top-0 z-40 backdrop-blur-xl bg-slate-950/90 border-b border-slate-800/80 transition-all duration-300">
        <div className="max-w-6xl mx-auto px-3 sm:px-6 h-14 sm:h-16 flex items-center justify-between gap-2">
          
          {/* Brand Logo with Glow */}
          <div
            onClick={() => handleSelectTab('dashboard')}
            className="flex items-center gap-2 cursor-pointer select-none shrink-0"
          >
            <div className="relative w-8 h-8 sm:w-9 sm:h-9 rounded-xl bg-gradient-to-tr from-indigo-600 via-purple-600 to-pink-500 p-0.5 shadow-md shadow-indigo-500/20">
              <div className="w-full h-full bg-slate-950 rounded-[10px] flex items-center justify-center">
                <Sparkles className="w-4 h-4 text-indigo-400" />
              </div>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="text-sm sm:text-base font-black tracking-tight bg-gradient-to-r from-white via-indigo-100 to-indigo-300 bg-clip-text text-transparent">
                MnemoMind
              </span>
              <span className="text-[9px] font-bold uppercase px-1.5 py-0.2 rounded-full bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                PRO
              </span>
            </div>
          </div>

          {/* Center: Language Switcher with Compact Flags & Word Counts */}
          <div className="flex items-center p-1 rounded-xl bg-slate-900 border border-slate-800 shrink-0">
            <button
              id="btn-lang-ru-header"
              onClick={() => setTargetLanguage('ru')}
              className={`px-2 sm:px-2.5 py-1 rounded-lg text-xs font-bold transition-all flex items-center gap-1 cursor-pointer select-none ${
                targetLanguage === 'ru'
                  ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-md shadow-blue-500/25'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
              title={`Rus tili (${completedWordsRu.length} ta yodlangan)`}
            >
              <span>🇷🇺</span>
              <span className="text-[11px]">RU ({completedWordsRu.length})</span>
            </button>

            <button
              id="btn-lang-en-header"
              onClick={() => setTargetLanguage('en')}
              className={`px-2 sm:px-2.5 py-1 rounded-lg text-xs font-bold transition-all flex items-center gap-1 cursor-pointer select-none ${
                targetLanguage === 'en'
                  ? 'bg-gradient-to-r from-indigo-600 to-violet-600 text-white shadow-md shadow-indigo-500/25'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
              title={`Ingliz tili (${completedWordsEn.length} ta yodlangan)`}
            >
              <span>🇬🇧</span>
              <span className="text-[11px]">EN ({completedWordsEn.length})</span>
            </button>
          </div>

          {/* Right Controls: Search, Level, & Sleek Super Menyu */}
          <div className="flex items-center gap-1.5 sm:gap-2 shrink-0">
            
            {/* Quick Vocabulary / My Words Button */}
            <button
              onClick={() => handleSelectTab('vocabulary')}
              title="Mening Lug'atim & O'zlashtirilgan So'zlar"
              className={`w-9 h-9 rounded-xl border flex items-center justify-center transition-all cursor-pointer ${
                activeTab === 'vocabulary'
                  ? 'bg-indigo-500/20 text-indigo-300 border-indigo-500/50 shadow-md shadow-indigo-500/20'
                  : 'bg-slate-900 border-slate-800 text-slate-400 hover:text-indigo-400 hover:bg-slate-850'
              }`}
            >
              <BookMarked className="w-4 h-4" />
            </button>

            {/* Quick Search Button */}
            <button
              onClick={() => handleSelectTab('search')}
              title="Mnemotexnik qidiruv"
              className={`w-9 h-9 rounded-xl border flex items-center justify-center transition-all cursor-pointer ${
                activeTab === 'search'
                  ? 'bg-sky-500/20 text-sky-300 border-sky-500/50 shadow-md shadow-sky-500/20'
                  : 'bg-slate-900 border-slate-800 text-slate-400 hover:text-sky-400 hover:bg-slate-850'
              }`}
            >
              <Search className="w-4 h-4" />
            </button>

            {/* Level Dropdown Pill on tablet+ */}
            <div className="relative hidden md:block">
              <select
                value={selectedLevel}
                onChange={(e) => setSelectedLevel(e.target.value as WordLevel)}
                className="text-xs font-bold py-1.5 px-2 rounded-xl bg-slate-900 border border-slate-700 text-slate-200 cursor-pointer focus:outline-none"
              >
                <option value="BEGINNER">🟢 A1-A2</option>
                <option value="INTERMEDIATE">🟡 B1-B2</option>
                <option value="ADVANCED">🔴 C1-C2</option>
              </select>
            </div>

            {/* Streak Badge on desktop */}
            <div className="hidden lg:flex items-center gap-1 px-2.5 py-1 rounded-xl bg-amber-500/15 border border-amber-500/30 text-amber-300">
              <Flame className="w-3.5 h-3.5 text-amber-400" />
              <span className="text-xs font-bold font-mono">{stats.streak}</span>
            </div>

            {/* ⚡ SLEEK SUPER MENYU BUTTON (Never squished or cut-off!) */}
            <button
              id="btn-super-menu-toggle"
              onClick={() => setIsSuperMenuOpen(!isSuperMenuOpen)}
              className="h-9 px-3 rounded-xl bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white font-bold text-xs shadow-md shadow-indigo-500/25 flex items-center gap-1.5 hover:opacity-95 active:scale-95 transition-all cursor-pointer select-none shrink-0"
              title="Super Menyu"
            >
              {isSuperMenuOpen ? (
                <X className="w-4 h-4" />
              ) : (
                <Menu className="w-4 h-4" />
              )}
              <span className="hidden sm:inline">Super Menyu</span>
            </button>

          </div>

        </div>
      </header>

      {/* ⚡ SUPER MENYU SLIDING DRAWER & MODAL OVERLAY */}
      {isSuperMenuOpen && (
        <div className="fixed inset-0 z-50 overflow-hidden animate-fade-in">
          {/* Backdrop */}
          <div
            onClick={() => setIsSuperMenuOpen(false)}
            className="absolute inset-0 bg-slate-950/80 backdrop-blur-md transition-opacity cursor-pointer"
          />

          {/* Drawer Container */}
          <div className="absolute top-0 right-0 max-w-sm w-full h-full bg-slate-900/95 border-l border-slate-800 shadow-2xl p-5 overflow-y-auto flex flex-col justify-between animate-slide-left">
            
            <div className="space-y-4">
              {/* Drawer Header */}
              <div className="flex items-center justify-between border-b border-slate-800 pb-3">
                <div className="flex items-center gap-2">
                  <div className="p-1.5 rounded-xl bg-gradient-to-tr from-indigo-600 to-pink-600 text-white shadow-md">
                    <Sparkles className="w-4 h-4" />
                  </div>
                  <div>
                    <h3 className="text-sm font-black text-white">⚡ Super Menyu</h3>
                    <p className="text-[10px] text-slate-400">
                      Barcha darslar, o'yinlar va qidiruv
                    </p>
                  </div>
                </div>

                <button
                  onClick={() => setIsSuperMenuOpen(false)}
                  className="p-1.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white transition-all cursor-pointer"
                >
                  <X className="w-4 h-4" />
                </button>
              </div>

              {/* Quick Status Bar inside Drawer */}
              <div className="p-3 rounded-2xl bg-slate-950/80 border border-slate-800 flex items-center justify-between text-xs">
                <div className="flex items-center gap-2">
                  <span className="text-base">{targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧'}</span>
                  <div>
                    <div className="font-bold text-white text-xs">
                      {targetLanguage === 'ru' ? 'Rus tili' : 'Ingliz tili'}
                    </div>
                    <div className="text-[10px] text-slate-400">{selectedLevel}</div>
                  </div>
                </div>

                <div className="flex items-center gap-1 px-2.5 py-1 rounded-xl bg-amber-500/10 text-amber-400 font-bold border border-amber-500/20 text-xs">
                  <Flame className="w-3.5 h-3.5" />
                  <span>{stats.streak} kun</span>
                </div>
              </div>

              {/* Navigation Items Grid */}
              <div className="space-y-1.5">
                <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider px-1">
                  Bo'limni tanlang:
                </div>

                <div className="grid grid-cols-1 gap-1.5">
                  {menuItems.map((item) => {
                    const Icon = item.icon;
                    const isActive = activeTab === item.id;

                    return (
                      <button
                        key={item.id}
                        id={`btn-tab-${item.id}`}
                        onClick={() => handleSelectTab(item.id)}
                        className={`w-full p-3 rounded-2xl border text-left flex items-center justify-between transition-all duration-200 group cursor-pointer ${
                          isActive
                            ? 'bg-gradient-to-r from-indigo-900/60 to-purple-900/40 border-indigo-500/60 text-white shadow-md shadow-indigo-600/20'
                            : 'bg-slate-950/60 border-slate-800/80 text-slate-300 hover:bg-slate-800/60 hover:border-slate-700'
                        }`}
                      >
                        <div className="flex items-center gap-2.5">
                          <div
                            className={`w-9 h-9 rounded-xl flex items-center justify-center shadow-md ${
                              isActive
                                ? 'bg-indigo-600 text-white'
                                : 'bg-slate-900 border border-slate-800 text-indigo-400 group-hover:scale-105'
                            }`}
                          >
                            <Icon className="w-4 h-4" />
                          </div>

                          <div className="space-y-0.5">
                            <div className="text-xs font-bold text-white flex items-center gap-1.5">
                              <span>{item.title}</span>
                              {item.badge && (
                                <span className="text-[9px] px-1.5 py-0.2 rounded-full bg-indigo-500/20 text-indigo-300 border border-indigo-500/30 font-semibold">
                                  {item.badge}
                                </span>
                              )}
                            </div>
                            <div className="text-[10px] text-slate-400 line-clamp-1">
                              {item.subtitle}
                            </div>
                          </div>
                        </div>

                        <ArrowRight
                          className={`w-3.5 h-3.5 transition-transform ${
                            isActive
                              ? 'text-indigo-400 translate-x-1'
                              : 'text-slate-600 group-hover:text-slate-400'
                          }`}
                        />
                      </button>
                    );
                  })}
                </div>
              </div>
            </div>

            {/* Drawer Footer */}
            <div className="pt-3 border-t border-slate-800 text-center space-y-1">
              <div className="flex items-center justify-center gap-2 text-xs text-slate-400">
                <span>MnemoMind PRO</span>
                <span>•</span>
                <span className="text-emerald-400 font-semibold text-[11px]">Offline-Ready</span>
              </div>
            </div>

          </div>
        </div>
      )}
    </>
  );
};
