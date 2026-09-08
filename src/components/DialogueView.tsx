'use client';

import React, { useState, useEffect } from 'react';
import { useApp } from '@/context/AppContext';
import {
  Volume2,
  Play,
  Square,
  Sparkles,
  MessageSquareQuote,
  CheckCircle2,
  ArrowRight,
  Headphones,
} from 'lucide-react';
import { audioManager } from '@/utils/audio';

export const DialogueView: React.FC = () => {
  const {
    targetLanguage,
    activeDialogue,
    selectedLevel,
    speakWord,
    setActiveTab,
  } = useApp();

  const [activeLineIndex, setActiveLineIndex] = useState<number | null>(null);
  const [isPlayingFull, setIsPlayingFull] = useState<boolean>(false);

  useEffect(() => {
    // Reset when dialogue changes
    setActiveLineIndex(null);
    setIsPlayingFull(false);
    audioManager.stop();
  }, [activeDialogue?.id]);

  if (!activeDialogue) {
    return (
      <div className="p-8 text-center text-slate-400 bg-slate-900/50 rounded-2xl border border-slate-800">
        Ushbu daraja uchun dialog topilmadi.
      </div>
    );
  }

  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';

  const handlePlayLine = (idx: number, text: string) => {
    setIsPlayingFull(false);
    setActiveLineIndex(idx);
    speakWord(text, () => {
      setActiveLineIndex(null);
    });
  };

  const handlePlayFullDialogue = () => {
    if (isPlayingFull) {
      audioManager.stop();
      setIsPlayingFull(false);
      setActiveLineIndex(null);
      return;
    }

    setIsPlayingFull(true);
    let currentIdx = 0;

    const playNext = () => {
      if (currentIdx >= activeDialogue.lines.length) {
        setIsPlayingFull(false);
        setActiveLineIndex(null);
        audioManager.playSuccessSound();
        return;
      }

      setActiveLineIndex(currentIdx);
      const lineText = activeDialogue.lines[currentIdx].textTarget;
      currentIdx++;

      speakWord(lineText, () => {
        // slight pause between dialogue lines for natural conversation feel
        setTimeout(playNext, 600);
      });
    };

    playNext();
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6 pb-28 animate-fade-in">
      
      {/* Dialogue Header Card */}
      <div className="p-6 rounded-3xl bg-gradient-to-br from-slate-900 via-slate-900/90 to-emerald-950/40 border border-emerald-500/30 shadow-xl relative overflow-hidden">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="space-y-1.5">
            <div className="flex items-center gap-2">
              <span className="p-1.5 rounded-lg bg-emerald-500/20 text-emerald-400">
                <Headphones className="w-4 h-4" />
              </span>
              <span className="text-xs font-bold uppercase tracking-wider text-emerald-400">
                {langFlag} Real Dialog & Speaking Practice
              </span>
            </div>
            <h2 className="text-xl sm:text-2xl font-black text-white">
              {activeDialogue.title}
            </h2>
            <p className="text-xs text-slate-400 italic">
              📍 Vaziyat: {activeDialogue.situationUz}
            </p>
          </div>

          {/* Full Dialogue Play/Stop Button */}
          <button
            onClick={handlePlayFullDialogue}
            className={`flex items-center justify-center gap-2 px-5 py-3 rounded-2xl text-xs font-bold transition-all shadow-lg cursor-pointer select-none shrink-0 ${
              isPlayingFull
                ? 'bg-rose-600 text-white shadow-rose-600/30 animate-pulse'
                : 'bg-gradient-to-r from-emerald-600 to-teal-600 text-white shadow-emerald-600/30 hover:scale-105'
            }`}
          >
            {isPlayingFull ? <Square className="w-4 h-4 fill-current" /> : <Play className="w-4 h-4 fill-current" />}
            <span>{isPlayingFull ? 'To\'xtatish' : 'Butun dialogni tinglash'}</span>
          </button>
        </div>
      </div>

      {/* Target Words Chips */}
      <div className="p-4 rounded-2xl bg-slate-900/60 border border-slate-800 space-y-2">
        <div className="flex items-center gap-2 text-xs font-bold text-indigo-400">
          <Sparkles className="w-4 h-4" />
          <span>Dialogda ishlatilgan bugungi kalit so'zlar:</span>
        </div>
        <div className="flex flex-wrap gap-1.5">
          {activeDialogue.targetWords.map((word, i) => (
            <span
              key={i}
              onClick={() => speakWord(word)}
              className="px-2.5 py-1 rounded-lg bg-slate-800 hover:bg-indigo-600/30 hover:text-indigo-300 text-slate-300 text-xs font-medium cursor-pointer transition-all border border-slate-700/60"
            >
              {word}
            </span>
          ))}
        </div>
      </div>

      {/* Chat Conversation Lines */}
      <div className="space-y-4">
        {activeDialogue.lines.map((line, idx) => {
          const isCurrentActive = activeLineIndex === idx;
          const isEven = idx % 2 === 0;

          return (
            <div
              key={idx}
              className={`flex items-start gap-3 transition-all duration-300 ${
                isEven ? 'flex-row' : 'flex-row-reverse'
              }`}
            >
              {/* Speaker Avatar Icon */}
              <div
                className={`w-11 h-11 rounded-2xl flex items-center justify-center text-xl shrink-0 shadow-md ${
                  isCurrentActive
                    ? 'bg-emerald-500/30 border-2 border-emerald-400 scale-110 animate-bounce'
                    : 'bg-slate-800 border border-slate-700'
                }`}
              >
                {line.speakerIcon}
              </div>

              {/* Chat Speech Bubble */}
              <div
                onClick={() => handlePlayLine(idx, line.textTarget)}
                className={`max-w-[85%] sm:max-w-[80%] p-4 rounded-2xl cursor-pointer transition-all duration-300 border ${
                  isCurrentActive
                    ? 'bg-emerald-950/70 border-emerald-400/80 shadow-xl shadow-emerald-500/20 scale-[1.02]'
                    : isEven
                    ? 'bg-slate-900/90 border-slate-800 hover:border-slate-700 hover:bg-slate-850'
                    : 'bg-indigo-950/40 border-indigo-900/40 hover:border-indigo-800 hover:bg-indigo-950/60'
                }`}
              >
                <div className="flex items-center justify-between gap-3 mb-1.5">
                  <span className="text-xs font-bold text-slate-400">
                    {line.speaker}
                  </span>
                  <Volume2
                    className={`w-4 h-4 transition-colors ${
                      isCurrentActive ? 'text-emerald-400 animate-pulse' : 'text-slate-500 hover:text-slate-300'
                    }`}
                  />
                </div>

                {/* Target Language Speech */}
                <p className="text-sm sm:text-base font-semibold text-white leading-snug">
                  {langFlag} "{line.textTarget}"
                </p>

                {/* Uzbek Subtitle */}
                <p className="text-xs text-slate-400 mt-2 italic pt-2 border-t border-slate-800/80">
                  🇺🇿 "{line.textUz}"
                </p>
              </div>
            </div>
          );
        })}
      </div>

      {/* Action Footer */}
      <div className="p-5 rounded-2xl bg-gradient-to-r from-slate-900 to-indigo-950/50 border border-slate-800 flex items-center justify-between gap-4">
        <div>
          <h4 className="text-sm font-bold text-white">
            Dialogdagi so'zlarni sinab ko'rmoqchimisiz?
          </h4>
          <p className="text-xs text-slate-400">
            5 ta mustahkamlovchi test orqali o'zlashtirishni tekshiring.
          </p>
        </div>

        <button
          onClick={() => setActiveTab('practice')}
          className="flex items-center gap-2 px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold shadow-lg shadow-indigo-600/30 transition-all cursor-pointer shrink-0"
        >
          <span>5 ta Test</span>
          <ArrowRight className="w-4 h-4" />
        </button>
      </div>

    </div>
  );
};
