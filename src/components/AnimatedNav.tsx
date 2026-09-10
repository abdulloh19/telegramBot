'use client';

import React from 'react';
import { useApp } from '@/context/AppContext';
import { AppTab } from '@/types';
import {
  LayoutDashboard,
  Sparkles,
  BookOpen,
  Gamepad2,
  MessageSquareQuote,
  CheckCircle2,
  Trophy,
  Search,
  BookMarked,
} from 'lucide-react';

interface NavItem {
  id: AppTab;
  label: string;
  sublabel: string;
  icon: React.ComponentType<{ className?: string }>;
  badge?: string;
  color: string;
}

export const AnimatedNav: React.FC = () => {
  const { activeTab, setActiveTab, stats } = useApp();

  const navItems: NavItem[] = [
    {
      id: 'dashboard',
      label: 'Asosiy',
      sublabel: 'Dashboard',
      icon: LayoutDashboard,
      color: 'from-blue-500 to-indigo-600',
    },
    {
      id: 'lesson',
      label: '20 So\'z',
      sublabel: 'Daily Cards',
      icon: Sparkles,
      badge: '20',
      color: 'from-indigo-500 to-purple-600',
    },
    {
      id: 'games',
      label: 'O\'yinlar',
      sublabel: 'Games Hub',
      icon: Gamepad2,
      badge: 'XP',
      color: 'from-emerald-500 to-teal-600',
    },
    {
      id: 'grammar',
      label: 'Grammatika',
      sublabel: 'Qoidalar',
      icon: BookOpen,
      color: 'from-pink-500 to-rose-600',
    },
    {
      id: 'dialogue',
      label: 'Dialog',
      sublabel: 'Speaking',
      icon: MessageSquareQuote,
      badge: 'Audio',
      color: 'from-cyan-500 to-blue-600',
    },
    {
      id: 'practice',
      label: 'Mashqlar',
      sublabel: 'Exercises',
      icon: CheckCircle2,
      badge: '5 ta',
      color: 'from-violet-500 to-fuchsia-600',
    },
    {
      id: 'quiz',
      label: 'Viktorina',
      sublabel: 'Speed Quiz',
      icon: Trophy,
      badge: `${stats.quizScores}p`,
      color: 'from-amber-500 to-orange-600',
    },
    {
      id: 'vocabulary',
      label: 'Lug\'atim',
      sublabel: 'Words',
      icon: BookMarked,
      badge: `${stats.wordsLearnedCount}`,
      color: 'from-blue-500 to-indigo-600',
    },
    {
      id: 'search',
      label: 'Qidiruv',
      sublabel: 'Dictionary',
      icon: Search,
      color: 'from-sky-500 to-cyan-600',
    },
  ];

  return (
    <nav aria-label="Asosiy navigatsiya menyusi" className="fixed bottom-2 sm:bottom-5 left-1/2 -translate-x-1/2 z-50 w-[98%] max-w-2xl">
      <div className="relative p-1.5 sm:p-2 rounded-2xl sm:rounded-3xl bg-slate-900/90 backdrop-blur-2xl border border-slate-700/60 shadow-2xl shadow-black/80 flex items-center justify-between gap-1 overflow-hidden">
        
        {/* Subtle animated background glow behind active element */}
        <div className="absolute inset-0 bg-gradient-to-r from-indigo-500/10 via-purple-500/10 to-pink-500/10 pointer-events-none" />

        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;

          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`relative flex-1 py-2 sm:py-2.5 px-1 rounded-xl sm:rounded-2xl transition-all duration-300 flex flex-col items-center justify-center gap-1 group cursor-pointer select-none ${
                isActive
                  ? 'text-white scale-105'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/40'
              }`}
            >
              {/* Active animated floating pill highlight */}
              {isActive && (
                <div
                  className={`absolute inset-0 rounded-xl sm:rounded-2xl bg-gradient-to-tr ${item.color} shadow-lg shadow-indigo-500/25 -z-10 animate-fade-in transition-all`}
                />
              )}

              {/* Icon with hover & active animation */}
              <div className="relative">
                <Icon
                  className={`w-5 h-5 transition-transform duration-300 ${
                    isActive ? 'scale-110 drop-shadow' : 'group-hover:scale-110'
                  }`}
                />
                {item.badge && !isActive && (
                  <span className="absolute -top-1.5 -right-2 px-1 py-0.2 rounded-full bg-indigo-500/30 text-[9px] font-mono text-indigo-300 border border-indigo-400/30 leading-none">
                    {item.badge}
                  </span>
                )}
              </div>

              {/* Label */}
              <span className={`text-[10px] sm:text-xs font-semibold tracking-tight transition-colors ${
                isActive ? 'text-white font-bold' : 'text-slate-400 group-hover:text-slate-300'
              }`}>
                {item.label}
              </span>

              {/* Active Dot Indicator */}
              {isActive && (
                <span className="w-1 h-1 rounded-full bg-white animate-ping absolute bottom-1" />
              )}
            </button>
          );
        })}

      </div>
    </nav>
  );
};
