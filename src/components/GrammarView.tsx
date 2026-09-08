'use client';

import React, { useState, useMemo } from 'react';
import { useApp } from '@/context/AppContext';
import { GRAMMAR_TOPICS } from '@/data/grammar';
import { GrammarTopic } from '@/types';
import {
  BookOpen,
  Sparkles,
  Volume2,
  CheckCircle2,
  XCircle,
  HelpCircle,
  Lightbulb,
  ArrowRight,
  Layers,
  ChevronRight,
  Zap,
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { audioManager } from '@/utils/audio';

export const GrammarView: React.FC = () => {
  const { targetLanguage, speakWord } = useApp();

  // Filter topics for the current language
  const topics: GrammarTopic[] = useMemo(() => {
    return GRAMMAR_TOPICS.filter(t => t.language === targetLanguage);
  }, [targetLanguage]);

  const [selectedTopicId, setSelectedTopicId] = useState<string>(topics[0]?.id || '');
  const [quizAnswer, setQuizAnswer] = useState<number | null>(null);
  const [isQuizAnswered, setIsQuizAnswered] = useState<boolean>(false);

  // Active topic
  const currentTopic = useMemo(() => {
    return topics.find(t => t.id === selectedTopicId) || topics[0];
  }, [topics, selectedTopicId]);

  // If topic changed or language changed, reset quiz state
  const handleSelectTopic = (topicId: string) => {
    audioManager.playClickSound();
    setSelectedTopicId(topicId);
    setQuizAnswer(null);
    setIsQuizAnswered(false);
  };

  const handleQuizSelect = (idx: number) => {
    if (isQuizAnswered || !currentTopic) return;
    setQuizAnswer(idx);
    setIsQuizAnswered(true);

    if (idx === currentTopic.quiz.correctIndex) {
      audioManager.playSuccessSound();
      confetti({
        particleCount: 35,
        spread: 55,
        origin: { y: 0.7 },
      });
    } else {
      audioManager.playErrorSound();
    }
  };

  if (!currentTopic) {
    return (
      <div className="p-8 text-center text-slate-400 bg-slate-900/50 rounded-2xl border border-slate-800">
        Grammatika mavzulari topilmadi.
      </div>
    );
  }

  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';
  const langTitle = targetLanguage === 'ru' ? 'Rus tili Grammatikasi' : 'Ingliz tili Grammatikasi';

  return (
    <div className="max-w-2xl mx-auto space-y-6 pb-28 animate-fade-in">
      
      {/* Top Section Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-xl">{langFlag}</span>
            <h2 className="text-lg sm:text-xl font-black text-white tracking-tight">
              {langTitle}
            </h2>
          </div>
          <p className="text-xs text-slate-400">
            Mnemotexnik qoidalar, oson formulalar va audio misollar
          </p>
        </div>

        <div className="px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold">
          {topics.length} ta Mavzu
        </div>
      </div>

      {/* Horizontal Scrollable Topic Tabs */}
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-none -mx-2 px-2">
        {topics.map((topic) => {
          const isActive = topic.id === currentTopic.id;
          return (
            <button
              key={topic.id}
              onClick={() => handleSelectTopic(topic.id)}
              className={`flex items-center gap-2 px-3.5 py-2.5 rounded-2xl text-xs font-semibold whitespace-nowrap transition-all shrink-0 cursor-pointer border ${
                isActive
                  ? 'bg-gradient-to-r from-indigo-600 to-purple-600 text-white border-indigo-500 shadow-lg shadow-indigo-500/25 scale-[1.02]'
                  : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-slate-200 hover:bg-slate-800/60'
              }`}
            >
              <span>{topic.icon}</span>
              <span>{topic.titleUz}</span>
            </button>
          );
        })}
      </div>

      {/* Active Topic Card */}
      <div className="p-5 sm:p-7 rounded-3xl bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-xl space-y-6">
        
        {/* Topic Title Header */}
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-[11px] font-bold uppercase tracking-wider text-indigo-400 flex items-center gap-1.5">
              <Layers className="w-3.5 h-3.5" />
              {currentTopic.category}
            </span>
            <span className="text-[10px] font-semibold px-2 py-0.5 rounded-full bg-purple-500/10 text-purple-300 border border-purple-500/20">
              {currentTopic.level}
            </span>
          </div>

          <h3 className="text-xl sm:text-2xl font-black text-white leading-tight">
            {currentTopic.icon} {currentTopic.title}
          </h3>
          <p className="text-xs text-slate-300 font-medium">
            {currentTopic.titleUz}
          </p>
        </div>

        {/* Mnemonic Golden Rule Banner */}
        <div className="p-4 rounded-2xl bg-gradient-to-r from-amber-500/15 via-purple-500/15 to-indigo-500/15 border border-amber-500/30 space-y-1.5 shadow-inner">
          <div className="flex items-center gap-2 text-xs font-bold text-amber-400">
            <Zap className="w-4 h-4 text-amber-400 animate-pulse" />
            <span>MNEMOTEXNIK ESLAB QOLISH QOIDASI:</span>
          </div>
          <p className="text-sm font-semibold text-amber-200 italic leading-snug">
            «{currentTopic.mnemonicRule}»
          </p>
        </div>

        {/* Formula Block (if available) */}
        {currentTopic.formula && (
          <div className="p-3.5 rounded-xl bg-slate-950/80 border border-slate-800 space-y-1">
            <div className="text-[10px] font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
              <Lightbulb className="w-3 h-3 text-indigo-400" />
              <span>Oson Formula:</span>
            </div>
            <div className="font-mono text-xs sm:text-sm font-bold text-indigo-300">
              {currentTopic.formula}
            </div>
          </div>
        )}

        {/* Detailed Explanation */}
        <div className="space-y-2">
          <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-1.5">
            <BookOpen className="w-3.5 h-3.5 text-indigo-400" />
            Batafsil Tushuntirish
          </h4>
          <div className="text-xs sm:text-sm text-slate-200 leading-relaxed whitespace-pre-line bg-slate-950/40 p-4 rounded-2xl border border-slate-800/80">
            {currentTopic.detailedExplanation}
          </div>
        </div>

        {/* Examples with Native Audio */}
        <div className="space-y-3">
          <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-1.5">
            <Volume2 className="w-3.5 h-3.5 text-emerald-400" />
            Amaliy Misollar (Audio bilan)
          </h4>

          <div className="space-y-2.5">
            {currentTopic.examples.map((ex, idx) => (
              <div
                key={idx}
                className="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 flex items-center justify-between gap-3 hover:border-slate-700 transition-all"
              >
                <div className="space-y-1 flex-1 min-w-0">
                  <div className="text-sm sm:text-base font-bold text-white leading-snug break-words">
                    {ex.target}
                  </div>
                  <div className="text-xs text-slate-400">
                    {ex.uzbek}
                  </div>
                  {ex.note && (
                    <span className="inline-block text-[10px] font-semibold text-indigo-400 bg-indigo-500/10 px-2 py-0.5 rounded-md mt-0.5">
                      {ex.note}
                    </span>
                  )}
                </div>

                <button
                  onClick={() => speakWord(ex.target)}
                  className="p-2.5 rounded-xl bg-indigo-500/20 text-indigo-400 hover:bg-indigo-500 hover:text-white transition-all cursor-pointer shrink-0 shadow-sm"
                  title="Audioni tinglash"
                >
                  <Volume2 className="w-4 h-4" />
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* Interactive Topic Quiz */}
        <div className="p-5 rounded-2xl bg-slate-950/80 border border-indigo-500/20 space-y-4">
          <div className="flex items-center gap-2 text-xs font-bold text-indigo-400 uppercase tracking-wider">
            <Sparkles className="w-4 h-4" />
            <span>Bilimingizni sinab ko'ring:</span>
          </div>

          <p className="text-sm font-bold text-white leading-snug">
            {currentTopic.quiz.question}
          </p>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
            {currentTopic.quiz.options.map((opt, idx) => {
              let btnStyle = 'bg-slate-900 border-slate-800 text-slate-200 hover:bg-slate-850 hover:border-slate-700';

              if (isQuizAnswered) {
                if (idx === currentTopic.quiz.correctIndex) {
                  btnStyle = 'bg-emerald-500/20 border-emerald-500 text-emerald-300 font-bold';
                } else if (idx === quizAnswer) {
                  btnStyle = 'bg-rose-500/25 border-rose-500 text-rose-300 line-through';
                } else {
                  btnStyle = 'bg-slate-900/40 border-slate-850 text-slate-500 opacity-40';
                }
              }

              return (
                <button
                  key={idx}
                  disabled={isQuizAnswered}
                  onClick={() => handleQuizSelect(idx)}
                  className={`p-3 rounded-xl border text-left text-xs sm:text-sm font-medium transition-all flex items-center justify-between gap-2 cursor-pointer ${btnStyle}`}
                >
                  <span>{opt}</span>
                  {isQuizAnswered && idx === currentTopic.quiz.correctIndex && (
                    <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
                  )}
                  {isQuizAnswered && idx === quizAnswer && idx !== currentTopic.quiz.correctIndex && (
                    <XCircle className="w-4 h-4 text-rose-400 shrink-0" />
                  )}
                </button>
              );
            })}
          </div>

          {/* Quiz Result & Explanation */}
          {isQuizAnswered && (
            <div className={`p-3.5 rounded-xl text-xs space-y-1 animate-fade-in ${
              quizAnswer === currentTopic.quiz.correctIndex
                ? 'bg-emerald-950/50 border border-emerald-500/30 text-emerald-300'
                : 'bg-rose-950/40 border border-rose-500/30 text-rose-200'
            }`}>
              <div className="font-bold flex items-center gap-1.5">
                {quizAnswer === currentTopic.quiz.correctIndex ? (
                  <>
                    <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                    <span>To'g'ri javob! Barakalla!</span>
                  </>
                ) : (
                  <>
                    <XCircle className="w-4 h-4 text-rose-400" />
                    <span>Noto'g'ri! Tushuntirish:</span>
                  </>
                )}
              </div>
              <p className="text-slate-300 leading-relaxed">
                {currentTopic.quiz.explanation}
              </p>
            </div>
          )}
        </div>

      </div>

    </div>
  );
};
