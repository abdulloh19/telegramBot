'use client';

import React from 'react';
import { useApp } from '@/context/AppContext';
import { Header } from '@/components/Header';
import { DashboardView } from '@/components/DashboardView';
import { DailyLessonView } from '@/components/DailyLessonView';
import { GrammarView } from '@/components/GrammarView';
import { GamesHubView } from '@/components/GamesHubView';
import { DialogueView } from '@/components/DialogueView';
import { PracticeView } from '@/components/PracticeView';
import { SpeedQuizView } from '@/components/SpeedQuizView';
import { SearchView } from '@/components/SearchView';

export default function Home() {
  const { activeTab } = useApp();

  return (
    <div className="relative min-h-screen pb-10 flex flex-col bg-[#030712] overflow-x-hidden">
      {/* Background ambient lighting */}
      <div className="fixed top-0 left-1/2 -translate-x-1/2 w-[500px] h-[300px] bg-indigo-600/10 rounded-full blur-[120px] pointer-events-none" />
      <div className="fixed bottom-0 left-1/4 w-[400px] h-[250px] bg-blue-600/10 rounded-full blur-[100px] pointer-events-none" />

      {/* Main Header with Super Menyu & Instant Search */}
      <Header />

      {/* Main Content Area with dynamic tab view */}
      <main className="flex-1 w-full max-w-2xl mx-auto px-3 pt-3 animate-fadeIn">
        {activeTab === 'dashboard' && <DashboardView />}
        {activeTab === 'lesson' && <DailyLessonView />}
        {activeTab === 'grammar' && <GrammarView />}
        {activeTab === 'games' && <GamesHubView />}
        {activeTab === 'dialogue' && <DialogueView />}
        {activeTab === 'practice' && <PracticeView />}
        {activeTab === 'quiz' && <SpeedQuizView />}
        {activeTab === 'search' && <SearchView />}
      </main>
    </div>
  );
}
