'use client';

import React, { useState, useEffect } from 'react';
import { useApp } from '@/context/AppContext';
import {
  Volume2,
  Play,
  Square,
  Sparkles,
  CheckCircle2,
  ArrowRight,
  Headphones,
  Check,
  Award,
  Layers,
} from 'lucide-react';
import { audioManager } from '@/utils/audio';

export const DialogueView: React.FC = () => {
  const {
    targetLanguage,
    availableDialogues,
    activeDialogue,
    selectedDialogueId,
    setSelectedDialogueId,
    completedDialogueIds,
    markDialogueCompleted,
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
        Ushbu til uchun dialog topilmadi.
      </div>
    );
  }

  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';
  const isCompleted = activeDialogue ? completedDialogueIds.has(activeDialogue.id) : false;

  const topics = [
    { id: 'taxi', label: 'Taksi (Taxi)', icon: '🚕' },
    { id: 'travel', label: 'Sayohat (Travel)', icon: '✈️' },
    { id: 'market', label: 'Do\'kon / Bozor (Market)', icon: '🛒' },
    { id: 'cafe', label: 'Kafe / Restoran (Café)', icon: '☕' },
    { id: 'pharmacy', label: 'Dorixona / Sog\'lik (Health)', icon: '💊' },
    { id: 'combo', label: 'Katta Birlashgan Dialog (Combo)', icon: '🌟' },
  ];

  const handleSelectCategory = (cat: string) => {
    const found = availableDialogues.find(d => d.category === cat);
    if (found) {
      setSelectedDialogueId(found.id);
    }
  };

  const handleNextTopic = () => {
    if (!activeDialogue) return;
    markDialogueCompleted(activeDialogue.id);
    audioManager.playSuccessSound();

    const currentIndex = availableDialogues.findIndex(d => d.id === activeDialogue.id);
    if (currentIndex >= 0 && currentIndex < availableDialogues.length - 1) {
      const nextDialog = availableDialogues[currentIndex + 1];
      setSelectedDialogueId(nextDialog.id);
    } else {
      // Loop or go to combo
      const combo = availableDialogues.find(d => d.category === 'combo');
      if (combo) setSelectedDialogueId(combo.id);
    }
  };

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
        markDialogueCompleted(activeDialogue.id);
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
    <div className="max-w-2xl mx-auto space-y-5 pb-28 animate-fade-in">
      
      {/* 5 Practical Life Topics Pill Selector */}
      <div className="space-y-2">
        <div className="flex items-center justify-between text-xs text-slate-300 font-bold px-1">
          <span className="flex items-center gap-1.5 text-indigo-300">
            <Layers className="w-4 h-4 text-indigo-400" />
            <span>Kundalik 5 Hayotiy Mavzu & Combo:</span>
          </span>
          <span className="text-[11px] text-slate-400">
            {completedDialogueIds.size} / {availableDialogues.length} o'rganildi
          </span>
        </div>

        <div className="flex items-center gap-2 overflow-x-auto pb-1.5 no-scrollbar scroll-smooth">
          {topics.map((t) => {
            const topicDialog = availableDialogues.find(d => d.category === t.id);
            const isSelected = activeDialogue?.category === t.id;
            const isTopicDone = topicDialog ? completedDialogueIds.has(topicDialog.id) : false;

            return (
              <button
                key={t.id}
                onClick={() => handleSelectCategory(t.id)}
                className={`px-3.5 py-2.5 rounded-2xl text-xs font-bold transition-all flex items-center gap-2 shrink-0 cursor-pointer border select-none ${
                  isSelected
                    ? 'bg-gradient-to-r from-indigo-600 to-purple-600 text-white border-indigo-400/80 shadow-lg shadow-indigo-600/30 scale-105'
                    : 'bg-slate-900/90 text-slate-300 border-slate-800 hover:border-slate-700 hover:bg-slate-850'
                }`}
              >
                <span className="text-base">{t.icon}</span>
                <span>{t.label}</span>
                {isTopicDone && (
                  <span className="w-4 h-4 rounded-full bg-emerald-500/30 text-emerald-400 border border-emerald-500/40 flex items-center justify-center text-[10px]">
                    ✓
                  </span>
                )}
              </button>
            );
          })}
        </div>
      </div>

      {/* Dialogue Header Card */}
      <div className="p-5 sm:p-6 rounded-3xl bg-gradient-to-br from-slate-900 via-slate-900/95 to-emerald-950/40 border border-emerald-500/30 shadow-xl relative overflow-hidden">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="space-y-1.5">
            <div className="flex items-center gap-2">
              <span className="p-1.5 rounded-lg bg-emerald-500/20 text-emerald-400">
                <Headphones className="w-4 h-4" />
              </span>
              <span className="text-xs font-bold uppercase tracking-wider text-emerald-400">
                {langFlag} Hayotiy Dialog & Audio
              </span>
              {isCompleted && (
                <span className="px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 text-[10px] font-extrabold flex items-center gap-1">
                  <Check className="w-3 h-3" /> O'rganildi
                </span>
              )}
            </div>
            <h2 className="text-lg sm:text-xl font-black text-white">
              {activeDialogue.title}
            </h2>
            <p className="text-xs text-slate-300 italic font-medium">
              📍 Vaziyat: {activeDialogue.situationUz}
            </p>
          </div>

          {/* Full Dialogue Play/Stop Button */}
          <button
            onClick={handlePlayFullDialogue}
            className={`flex items-center justify-center gap-2 px-4 py-2.5 rounded-2xl text-xs font-bold transition-all shadow-lg cursor-pointer select-none shrink-0 ${
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
      <div className="p-4 rounded-2xl bg-slate-900/70 border border-slate-800 space-y-2">
        <div className="flex items-center gap-2 text-xs font-bold text-indigo-400">
          <Sparkles className="w-4 h-4 text-amber-400" />
          <span>Ushbu mavzuda ishlatilgan kalit so'zlar:</span>
        </div>
        <div className="flex flex-wrap gap-1.5">
          {activeDialogue.targetWords.map((word, i) => (
            <span
              key={i}
              onClick={() => speakWord(word)}
              className="px-2.5 py-1 rounded-xl bg-slate-800 hover:bg-indigo-600/30 hover:text-indigo-300 text-slate-200 text-xs font-semibold cursor-pointer transition-all border border-slate-700/60 flex items-center gap-1 active:scale-95"
              title="Talaffuzni eshitish"
            >
              <Volume2 className="w-3 h-3 text-indigo-400" />
              <span>{word}</span>
            </span>
          ))}
        </div>
      </div>

      {/* Chat Conversation Lines */}
      <div className="space-y-3.5">
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
                    : 'bg-slate-800/90 border border-slate-700'
                }`}
              >
                {line.speakerIcon}
              </div>

              {/* Chat Speech Bubble */}
              <div
                onClick={() => handlePlayLine(idx, line.textTarget)}
                className={`max-w-[85%] sm:max-w-[80%] p-4 rounded-2xl cursor-pointer transition-all duration-300 border ${
                  isCurrentActive
                    ? 'bg-emerald-950/80 border-emerald-400 shadow-xl shadow-emerald-500/20 scale-[1.02]'
                    : isEven
                    ? 'bg-slate-900/90 border-slate-800 hover:border-slate-700 hover:bg-slate-850'
                    : 'bg-indigo-950/40 border-indigo-900/40 hover:border-indigo-800 hover:bg-indigo-950/60'
                }`}
              >
                <div className="flex items-center justify-between gap-3 mb-1.5">
                  <span className="text-xs font-bold text-slate-300">
                    {line.speaker}
                  </span>
                  <div className="flex items-center gap-1 text-xs text-slate-400">
                    <Volume2
                      className={`w-4 h-4 transition-colors ${
                        isCurrentActive ? 'text-emerald-400 animate-pulse' : 'text-slate-500 hover:text-slate-300'
                      }`}
                    />
                    <span className="text-[10px] text-slate-500">Tinglash</span>
                  </div>
                </div>

                {/* Target Language Speech */}
                <p className="text-sm sm:text-base font-semibold text-white leading-relaxed">
                  {langFlag} "{line.textTarget}"
                </p>

                {/* Uzbek Subtitle */}
                <p className="text-xs text-slate-300 mt-2 italic pt-2 border-t border-slate-800/80 font-medium">
                  🇺🇿 "{line.textUz}"
                </p>
              </div>
            </div>
          );
        })}
      </div>

      {/* Completion & Next Topic Progression Footer */}
      <div className="p-5 rounded-3xl bg-gradient-to-br from-slate-900 via-indigo-950/40 to-slate-900 border border-indigo-500/30 shadow-xl space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
          <div>
            <div className="flex items-center gap-2">
              <Award className="w-5 h-5 text-amber-400" />
              <h4 className="text-sm sm:text-base font-black text-white">
                Mavzuni o'zlashtirdingizmi?
              </h4>
            </div>
            <p className="text-xs text-slate-300 mt-0.5 font-medium">
              Dialogdagi barcha so'zlar keyingi darsdagi birlashgan dialogga qo'shiladi!
            </p>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={handleNextTopic}
              className="flex items-center justify-center gap-2 px-5 py-3 rounded-2xl bg-gradient-to-r from-emerald-600 via-teal-600 to-indigo-600 hover:from-emerald-500 hover:to-indigo-500 text-white text-xs font-black shadow-lg shadow-emerald-500/25 transition-all cursor-pointer active:scale-95 shrink-0"
            >
              <CheckCircle2 className="w-4 h-4" />
              <span>Mavzuni yakunlash (+30 XP) & Keyingi dialog</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>

    </div>
  );
};
