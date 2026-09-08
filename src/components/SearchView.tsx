'use client';

import React, { useState, useMemo } from 'react';
import { useApp } from '@/context/AppContext';
import { WordLevel } from '@/types';
import { MNEMONIC_WORDS } from '@/data/words';
import { Search, Volume2, Sparkles, Filter, BookOpen, AlertCircle, X, Check } from 'lucide-react';

export const SearchView: React.FC = () => {
  const { targetLanguage, speakWord } = useApp();
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedLevel, setSelectedLevel] = useState<WordLevel | 'ALL'>('ALL');

  // Filter current language words
  const langWords = useMemo(() => {
    return MNEMONIC_WORDS.filter(w => w.language === targetLanguage);
  }, [targetLanguage]);

  // Search filtered results
  const filtered = useMemo(() => {
    let result = langWords;

    if (selectedLevel !== 'ALL') {
      result = result.filter(w => w.level === selectedLevel);
    }

    if (!searchTerm.trim()) {
      return result;
    }

    const term = searchTerm.toLowerCase().trim();
    return result.filter(w => 
      w.word.toLowerCase().includes(term) ||
      w.uzbekMeaning.toLowerCase().includes(term) ||
      w.mnemonicStory.toLowerCase().includes(term) ||
      w.mnemonicHook.toLowerCase().includes(term) ||
      w.exampleTarget.toLowerCase().includes(term) ||
      w.exampleUz.toLowerCase().includes(term)
    );
  }, [langWords, selectedLevel, searchTerm]);

  const quickTags = [
    { label: '🛍 Supermarket', query: 'supermarket' },
    { label: '🚕 Taksi / Yo\'l', query: 'taksi' },
    { label: '☕ Restoran / Kafe', query: 'kafe' },
    { label: '👋 Kundalik / Salom', query: 'salom' },
    { label: '🔥 Mashhur so\'zlar', query: '' },
  ];

  const handleTagClick = (tagQuery: string) => {
    setSearchTerm(tagQuery);
  };

  const langTitle = targetLanguage === 'ru' ? 'Ruscha' : 'Inglizcha';
  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';

  return (
    <div className="max-w-2xl mx-auto p-4 space-y-5 pb-16 animate-fade-in">
      {/* Search Header */}
      <div className="space-y-1">
        <div className="flex items-center gap-2">
          <div className="p-2 rounded-xl bg-sky-500/10 text-sky-400 border border-sky-500/20">
            <Search className="w-5 h-5" />
          </div>
          <div>
            <h2 className="text-xl font-black text-white flex items-center gap-2">
              <span>Mnemotexnik So'z Qidiruvi</span>
              <span className="text-xs px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                {langFlag} {langTitle}
              </span>
            </h2>
            <p className="text-xs text-slate-400">
              So'z, o'zbekcha tarjimasi yoki eshitilish ilmog'i bo'yicha darhol qidiring
            </p>
          </div>
        </div>
      </div>

      {/* Search Input Box with Action Button */}
      <div className="flex items-center gap-2">
        <div className="relative flex-1">
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-500">
            <Search className="w-4 h-4 text-sky-400" />
          </div>
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                e.currentTarget.blur();
              }
            }}
            placeholder={`${langTitle}, o'zbekcha so'z yoki talaffuzini yozing...`}
            className="w-full pl-10 pr-10 py-3.5 bg-slate-900/90 border border-slate-700/80 focus:border-sky-500 rounded-2xl text-sm text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-sky-500/20 transition-all shadow-inner"
          />
          {searchTerm && (
            <button
              onClick={() => setSearchTerm('')}
              className="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-white cursor-pointer"
              title="Tozalash"
            >
              <X className="w-4 h-4" />
            </button>
          )}
        </div>

        <button
          onClick={() => {
            // Already reacts in real time, but button gives tactile confirmation
          }}
          className="px-5 py-3.5 rounded-2xl bg-gradient-to-r from-sky-500 to-indigo-600 hover:from-sky-600 hover:to-indigo-700 text-white font-bold text-xs shadow-lg shadow-sky-500/25 transition-all flex items-center gap-1.5 shrink-0 cursor-pointer"
        >
          <Search className="w-4 h-4" />
          <span>Qidirish</span>
        </button>
      </div>

      {/* Quick Search Suggestions */}
      <div className="space-y-1.5">
        <div className="text-[11px] text-slate-400 font-semibold">Tezkor mavzular:</div>
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1">
          {quickTags.map((tag) => (
            <button
              key={tag.label}
              onClick={() => handleTagClick(tag.query)}
              className="px-3 py-1.5 rounded-xl bg-slate-800/80 hover:bg-slate-700 text-slate-300 hover:text-white text-xs font-medium border border-slate-700/60 transition-all shrink-0 cursor-pointer"
            >
              {tag.label}
            </button>
          ))}
        </div>
      </div>

      {/* Level Filters */}
      <div className="flex items-center gap-1.5 overflow-x-auto pb-1 text-xs">
        <div className="text-slate-500 flex items-center gap-1 pr-1 font-semibold">
          <Filter className="w-3.5 h-3.5" />
          Daraja:
        </div>
        {(['ALL', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'] as const).map((lvl) => {
          const isSelected = selectedLevel === lvl;
          const label = lvl === 'ALL' ? 'Hammasi' : lvl === 'BEGINNER' ? 'A1-A2' : lvl === 'INTERMEDIATE' ? 'B1-B2' : 'C1-C2';
          return (
            <button
              key={lvl}
              onClick={() => setSelectedLevel(lvl)}
              className={`px-3 py-1.5 rounded-xl font-bold transition-all shrink-0 cursor-pointer ${
                isSelected
                  ? 'bg-sky-600 text-white shadow-md shadow-sky-600/30'
                  : 'bg-slate-800/80 text-slate-400 hover:text-slate-200 border border-slate-700/50'
              }`}
            >
              {label}
            </button>
          );
        })}
      </div>

      {/* Count Indicator */}
      <div className="text-xs text-slate-500 flex items-center justify-between px-1">
        <span>Topilgan so'zlar: <strong className="text-slate-200">{filtered.length}</strong> ta</span>
        <span className="text-[10px] bg-emerald-500/10 text-emerald-400 px-2.5 py-0.5 rounded-full border border-emerald-500/20 font-semibold">
          ⚡ 0ms Chaqmoqdek Tez
        </span>
      </div>

      {/* Results List */}
      <div className="space-y-3">
        {filtered.length === 0 ? (
          <div className="p-8 text-center bg-slate-900/50 border border-slate-800/60 rounded-3xl space-y-2">
            <AlertCircle className="w-8 h-8 text-slate-500 mx-auto" />
            <p className="text-sm font-semibold text-slate-300">"{searchTerm}" bo'yicha so'z topilmadi</p>
            <p className="text-xs text-slate-400">
              Boshqa so'z, tarjima yoki assotsiatsiya kaliti orqali qidirib ko'ring.
            </p>
            <button
              onClick={() => setSearchTerm('')}
              className="mt-2 px-4 py-2 rounded-xl bg-slate-800 text-xs text-indigo-400 hover:text-white font-semibold cursor-pointer"
            >
              Barcha so'zlarni ko'rish
            </button>
          </div>
        ) : (
          filtered.map((word) => (
            <div
              key={word.id}
              className="bg-slate-900/80 border border-slate-800/90 hover:border-slate-700 rounded-2xl p-4 shadow-lg transition-all space-y-3 group"
            >
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg font-black text-white">{word.word}</span>
                    <button
                      onClick={() => speakWord(word.word)}
                      className="p-1.5 text-indigo-400 hover:text-white bg-indigo-500/10 hover:bg-indigo-500/20 rounded-lg transition-all cursor-pointer"
                      title="Ovoz chiqarish"
                    >
                      <Volume2 className="w-4 h-4" />
                    </button>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded-md bg-slate-800 text-slate-400 border border-slate-700/50">
                      {word.level === 'BEGINNER' ? 'A1-A2' : word.level === 'INTERMEDIATE' ? 'B1-B2' : 'C1-C2'}
                    </span>
                  </div>
                  <div className="text-xs text-slate-400 mt-0.5 font-mono">{word.pronunciation}</div>
                </div>

                <div className="text-right">
                  <div className="text-sm font-bold text-amber-400">🇺🇿 {word.uzbekMeaning}</div>
                </div>
              </div>

              {/* Mnemonic Hook */}
              <div className="p-2.5 bg-indigo-950/30 border border-indigo-900/40 rounded-xl text-xs space-y-1">
                <div className="flex items-center gap-1 text-[11px] font-bold text-indigo-300">
                  <Sparkles className="w-3.5 h-3.5 text-amber-400" />
                  Assotsiatsiya: <span className="text-amber-300 font-semibold">«{word.mnemonicHook}»</span>
                </div>
                <p className="text-slate-300 italic text-[11px] leading-relaxed">
                  "{word.mnemonicStory}"
                </p>
              </div>

              {/* Example */}
              <div className="text-[11px] text-slate-400 border-t border-slate-800/80 pt-2 flex items-start gap-1.5">
                <BookOpen className="w-3.5 h-3.5 text-slate-500 shrink-0 mt-0.5" />
                <div>
                  <span className="text-slate-200 font-medium">{word.exampleTarget}</span>
                  <span className="block text-slate-400 text-[10px]">{word.exampleUz}</span>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
