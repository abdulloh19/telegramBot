'use client';

import React, { useEffect, useState, useRef } from 'react';
import { useApp } from '@/context/AppContext';
import {
  Flame,
  Sparkles,
  Volume2,
  ArrowRight,
  BrainCircuit,
  MessageSquareQuote,
  CheckCircle2,
  Trophy,
  Zap,
  BookMarked,
  Users,
  UserCheck,
  BellRing,
  RefreshCw,
  ExternalLink,
  ShieldCheck,
  Lock,
  Eye,
  Filter,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';

interface BotUser {
  id: string;
  chatId: number;
  firstName: string;
  username: string | null;
  hasUsername: boolean;
  displayIdentifier: string;
  phoneNumber: string | null;
  language: string;
  languageName: string;
  languageFlag: string;
  level: string;
  streak: number;
  wordsLearned: number;
  currentDayIndex: number;
  currentWordInDay: number;
  lastActiveDate: string;
  daysSinceLastActive: number;
  lastReminderDate: string | null;
  enteredAfterReminder: boolean;
  statusBadge: string;
  statusColor: string;
}

interface UsersApiResponse {
  success: boolean;
  totalUsers: number;
  activeTodayCount: number;
  enteredAfterReminderCount: number;
  users: BotUser[];
  loadedFrom?: string;
  timestamp: string;
}

export const DashboardView: React.FC = () => {
  const {
    targetLanguage,
    selectedLevel,
    setActiveTab,
    stats,
    activeWords,
    currentWordIndex,
    speakWord,
    isAudioPlaying,
  } = useApp();

  const [usersData, setUsersData] = useState<UsersApiResponse | null>(null);
  const [isLoadingUsers, setIsLoadingUsers] = useState<boolean>(false);
  const [userFilter, setUserFilter] = useState<'ALL' | 'AFTER_REMINDER' | 'TODAY_ACTIVE' | 'AT_RISK'>('ALL');
  
  // Filter horizontal scroll reference
  const filterScrollRef = useRef<HTMLDivElement>(null);

  const scrollFilter = (direction: 'left' | 'right') => {
    if (filterScrollRef.current) {
      const offset = direction === 'left' ? -200 : 200;
      filterScrollRef.current.scrollBy({ left: offset, behavior: 'smooth' });
    }
  };

  // Admin view state
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [adminPinInput, setAdminPinInput] = useState<string>('');
  const [showPinDialog, setShowPinDialog] = useState<boolean>(false);

  // Check if current user is admin
  useEffect(() => {
    try {
      const tgUser = (window as unknown as { Telegram?: { WebApp?: { initDataUnsafe?: { user?: { id?: number; username?: string } } } } })
        ?.Telegram?.WebApp?.initDataUnsafe?.user;

      const adminId = 5787141744;
      const adminUsernames = ['abu', 'parij', 'abu_dev'];

      const isTgAdmin =
        tgUser?.id === adminId ||
        (tgUser?.username && adminUsernames.includes(tgUser.username.toLowerCase()));

      const savedAdmin = localStorage.getItem('mnemo_admin_authenticated') === 'true';

      if (isTgAdmin || savedAdmin) {
        setIsAdmin(true);
      }
    } catch {
      // ignore
    }
  }, []);

  const fetchUsers = async () => {
    setIsLoadingUsers(true);
    try {
      const res = await fetch('/api/users', { cache: 'no-store' });
      if (res.ok) {
        const data = await res.json();
        setUsersData(data);
      }
    } catch (e) {
      console.error('Failed to fetch bot users:', e);
    } finally {
      setIsLoadingUsers(false);
    }
  };

  useEffect(() => {
    fetchUsers();
    // Real-time polling every 5 seconds for live sync
    const interval = setInterval(fetchUsers, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleAdminUnlock = () => {
    // PIN or direct unlock
    if (adminPinInput.trim() === '7777' || adminPinInput.trim() === 'admin' || adminPinInput.trim() === '5787141744') {
      setIsAdmin(true);
      localStorage.setItem('mnemo_admin_authenticated', 'true');
      setShowPinDialog(false);
      setAdminPinInput('');
    } else {
      alert('Noto\'g\'ri parol! (Iltimos, Telegram bot orqali kiring yoki to\'g\'ri admin kodini kiriting)');
    }
  };

  const featuredWord = activeWords.length > 0 ? activeWords[0] : null;

  const handlePlayFeatured = () => {
    if (featuredWord) {
      speakWord(featuredWord.word);
    }
  };

  const levelText = {
    BEGINNER: "Boshlang'ich (A1-A2)",
    INTERMEDIATE: "O'rta (B1-B2)",
    ADVANCED: 'Yuqori (C1-C2)',
  }[selectedLevel];

  const langFlag = targetLanguage === 'ru' ? '🇷🇺' : '🇬🇧';
  const langTitle = targetLanguage === 'ru' ? 'Rus Tili' : 'Ingliz Tili';

  // Filtered users for Admin CRM
  const filteredUsers = (usersData?.users || []).filter((u) => {
    if (userFilter === 'AFTER_REMINDER') return u.enteredAfterReminder;
    if (userFilter === 'TODAY_ACTIVE') return u.daysSinceLastActive === 0;
    if (userFilter === 'AT_RISK') return u.daysSinceLastActive >= 2;
    return true;
  });

  return (
    <div className="space-y-5 pb-16 animate-fade-in">
      
      {/* Hero Welcome Banner */}
      <div className="relative overflow-hidden rounded-3xl p-5 sm:p-7 bg-gradient-to-br from-indigo-950/70 via-slate-900/90 to-purple-950/60 border border-slate-700/70 shadow-2xl backdrop-blur-xl">
        <div className="absolute -top-24 -right-24 w-60 h-60 bg-indigo-500/15 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-24 -left-24 w-60 h-60 bg-purple-500/15 rounded-full blur-3xl pointer-events-none" />

        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="space-y-2">
            <div className="inline-flex items-center gap-2.5 px-3.5 py-1.5 rounded-full bg-indigo-500/20 border border-indigo-500/30 text-indigo-300 text-xs sm:text-sm font-bold">
              <span className="text-base">{langFlag}</span>
              <span>{langTitle} Mnemonik Tizimi</span>
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
              <span className="text-xs text-emerald-400 font-extrabold">Jonli Rejim</span>
            </div>

            <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black tracking-tight text-white leading-snug">
              Assalomu alaykum,{' '}
              <span className="bg-gradient-to-r from-indigo-400 via-purple-300 to-pink-400 bg-clip-text text-transparent">
                Do'stim!
              </span>{' '}
              👋
            </h1>

            <p className="text-sm sm:text-base text-slate-200 max-w-xl leading-relaxed font-medium">
              So'zlarni quruq yodlash emas, balki <b>assotsiatsiya, fonetik ilmoq va jonli obrazlar</b> orqali 10 barobar tez va uzoq muddatga eslab qoling.
            </p>
          </div>

          {/* Quick Action Button */}
          <button
            onClick={() => setActiveTab('lesson')}
            className="flex items-center justify-center gap-2.5 px-6 py-3.5 rounded-2xl bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white font-extrabold text-sm sm:text-base shadow-xl shadow-indigo-600/30 hover:scale-[1.02] active:scale-[0.98] transition-all cursor-pointer shrink-0"
          >
            <span>
              {currentWordIndex > 0
                ? `Bugungi darsni davom ettirish (${currentWordIndex + 1}-so'zdan)`
                : "Bugungi 20 ta so'z darsi"}
            </span>
            <ArrowRight className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* 4 Statistics Cards Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 sm:gap-4">
        {/* Streak Card */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-lg flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs sm:text-sm text-slate-300 font-bold">Streak zanjiri</span>
            <div className="p-2 rounded-xl bg-amber-500/15 text-amber-400">
              <Flame className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-2.5">
            <div className="text-2xl sm:text-3xl font-black text-white font-mono">
              {stats.streak} <span className="text-sm font-bold text-amber-400">kun</span>
            </div>
            <div className="text-xs text-slate-400 font-medium mt-0.5">Rekord: {stats.maxStreak} kun</div>
          </div>
        </div>

        {/* Mastered Words */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-lg flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs sm:text-sm text-slate-300 font-bold">Yodlangan so'z</span>
            <div className="p-2 rounded-xl bg-indigo-500/15 text-indigo-400">
              <BookMarked className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-2.5">
            <div className="text-2xl sm:text-3xl font-black text-white font-mono">
              {stats.wordsLearnedCount}{' '}
              <span className="text-sm font-bold text-indigo-400">ta</span>
            </div>
            <div className="text-xs text-slate-400 font-medium mt-0.5">Mavjud: {activeWords.length} ta</div>
          </div>
        </div>

        {/* Exercises */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-lg flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs sm:text-sm text-slate-300 font-bold">Bajarilgan test</span>
            <div className="p-2 rounded-xl bg-emerald-500/15 text-emerald-400">
              <CheckCircle2 className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-2.5">
            <div className="text-2xl sm:text-3xl font-black text-white font-mono">
              {stats.exercisesCompletedCount}{' '}
              <span className="text-sm font-bold text-emerald-400">ta</span>
            </div>
            <div className="text-xs text-slate-400 font-medium mt-0.5">Aniqlik: 94%</div>
          </div>
        </div>

        {/* Quiz Scores */}
        <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 shadow-lg flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-xs sm:text-sm text-slate-300 font-bold">Reyting balli</span>
            <div className="p-2 rounded-xl bg-purple-500/15 text-purple-400">
              <Trophy className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-2.5">
            <div className="text-2xl sm:text-3xl font-black text-white font-mono">
              {stats.quizScores}{' '}
              <span className="text-sm font-bold text-purple-400">ochko</span>
            </div>
            <div className="text-xs text-slate-400 font-medium mt-0.5">Daraja: {selectedLevel}</div>
          </div>
        </div>
      </div>

      {/* Featured Word of the Day */}
      {featuredWord && (
        <div className="p-5 sm:p-7 rounded-3xl bg-gradient-to-r from-slate-900 via-slate-900/95 to-indigo-950/40 border border-indigo-500/30 shadow-xl space-y-4">
          <div className="flex items-center justify-between gap-2">
            <div className="flex items-center gap-2.5">
              <span className="p-2 rounded-xl bg-indigo-500/20 text-indigo-400">
                <Sparkles className="w-5 h-5" />
              </span>
              <div>
                <span className="text-xs sm:text-sm font-extrabold uppercase tracking-wider text-indigo-300">
                  Bugungi Kun So'zi ({langFlag})
                </span>
                <p className="text-xs text-slate-400 font-medium">{levelText}</p>
              </div>
            </div>

            <button
              onClick={handlePlayFeatured}
              className={`flex items-center gap-2 px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition-all cursor-pointer ${
                isAudioPlaying
                  ? 'bg-emerald-500 text-white animate-pulse'
                  : 'bg-indigo-600/30 text-indigo-300 border border-indigo-500/40 hover:bg-indigo-600 hover:text-white'
              }`}
            >
              <Volume2 className="w-4 h-4" />
              <span>{isAudioPlaying ? "O'qilmoqda..." : 'Ovozli tinglash'}</span>
            </button>
          </div>

          <div className="grid md:grid-cols-2 gap-4 items-center">
            <div className="space-y-1.5">
              <div className="flex items-baseline gap-2.5">
                <h3 className="text-3xl sm:text-4xl font-black tracking-tight text-white uppercase">
                  {featuredWord.word}
                </h3>
                <span className="text-sm sm:text-base font-mono text-slate-300">
                  {featuredWord.pronunciation}
                </span>
              </div>
              <p className="text-lg sm:text-xl font-black text-emerald-400">
                🇺🇿 {featuredWord.uzbekMeaning}
              </p>
            </div>

            <div className="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 space-y-2">
              <div className="flex items-center gap-1.5 text-xs sm:text-sm font-bold text-amber-400">
                <Zap className="w-4 h-4" />
                <span>Fonetik Ilmoq: «{featuredWord.mnemonicHook}»</span>
              </div>
              <p className="text-xs sm:text-sm text-slate-200 italic leading-relaxed">
                "{featuredWord.mnemonicStory}"
              </p>
            </div>
          </div>
        </div>
      )}

      {/* 👑 ADMIN-ONLY SECTION: FOYDALANUVCHILAR TAHLILI (CRM) */}
      {isAdmin ? (
        <div className="p-5 sm:p-7 rounded-3xl bg-slate-900/90 border border-indigo-500/40 shadow-2xl space-y-6 animate-fadeIn">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-slate-800 pb-4">
            <div className="space-y-1">
              <div className="flex items-center gap-3">
                <div className="p-2.5 rounded-2xl bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
                  <ShieldCheck className="w-6 h-6 text-emerald-400" />
                </div>
                <div>
                  <h3 className="text-lg sm:text-xl font-black text-white flex items-center gap-2.5 flex-wrap">
                    <span>👑 Admin CRM: Foydalanuvchilar Tahlili</span>
                    <span className="text-xs px-2.5 py-0.5 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 font-extrabold">
                      Faqat Sizga Ko'rinadi
                    </span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-300 font-medium">
                    Real-time Telegram usernamesi, o'rganayotgan tili va eslatmadan so'ng kirish monitoringi
                  </p>
                </div>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <button
                onClick={fetchUsers}
                disabled={isLoadingUsers}
                className="flex items-center gap-2 px-3.5 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 hover:text-white text-xs sm:text-sm font-bold border border-slate-700 transition-all cursor-pointer"
              >
                <RefreshCw className={`w-4 h-4 ${isLoadingUsers ? 'animate-spin' : ''}`} />
                <span>Yangilash</span>
              </button>
            </div>
          </div>

          {/* CRM Stat Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-3.5">
            <div className="p-4 rounded-2xl bg-indigo-950/40 border border-indigo-500/30 flex items-center justify-between">
              <div>
                <span className="text-xs sm:text-sm text-indigo-200 font-bold">Jami Haqiqiy Bot Foydalanuvchilari</span>
                <div className="text-3xl font-black text-white font-mono mt-1">
                  {usersData?.totalUsers ?? '0'} ta
                </div>
              </div>
              <div className="p-3 rounded-xl bg-indigo-500/20 text-indigo-400">
                <Users className="w-6 h-6" />
              </div>
            </div>

            <div className="p-4 rounded-2xl bg-emerald-950/40 border border-emerald-500/30 flex items-center justify-between">
              <div>
                <span className="text-xs sm:text-sm text-emerald-200 font-bold">Bugun Faollar</span>
                <div className="text-3xl font-black text-white font-mono mt-1">
                  {usersData?.activeTodayCount ?? '0'} ta
                </div>
              </div>
              <div className="p-3 rounded-xl bg-emerald-500/20 text-emerald-400">
                <UserCheck className="w-6 h-6" />
              </div>
            </div>

            <div className="p-4 rounded-2xl bg-purple-950/40 border border-purple-500/30 flex items-center justify-between">
              <div>
                <span className="text-xs sm:text-sm text-purple-200 font-bold">🟣 Eslatmadan So'ng Kirganlar</span>
                <div className="text-3xl font-black text-white font-mono mt-1">
                  {usersData?.enteredAfterReminderCount ?? '0'} ta
                </div>
              </div>
              <div className="p-3 rounded-xl bg-purple-500/20 text-purple-400">
                <BellRing className="w-6 h-6" />
              </div>
            </div>
          </div>

          {/* Filter Tabs with Left & Right scroll icons */}
          <div className="flex items-center gap-2">
            {/* Chapga scroll icon */}
            <button
              type="button"
              onClick={() => scrollFilter('left')}
              className="p-2.5 rounded-xl bg-slate-800/90 hover:bg-slate-700 text-indigo-300 hover:text-white border border-slate-700 shadow-md transition-all shrink-0 cursor-pointer flex items-center justify-center active:scale-95"
              title="Chapga surish"
            >
              <ChevronLeft className="w-5 h-5" />
            </button>

            <div
              ref={filterScrollRef}
              className="flex items-center gap-2 overflow-x-auto no-scrollbar scroll-smooth py-1 px-1 flex-1"
            >
              <div className="text-slate-300 flex items-center gap-1.5 pr-2 font-bold text-sm sm:text-base shrink-0">
                <Filter className="w-4 h-4 text-indigo-400" />
                Ko'rish:
              </div>
              {[
                { id: 'ALL' as const, label: `Barchasi (${usersData?.totalUsers ?? 0})` },
                { id: 'AFTER_REMINDER' as const, label: `🟣 Eslatmadan keyin kirganlar (${usersData?.enteredAfterReminderCount ?? 0})` },
                { id: 'TODAY_ACTIVE' as const, label: `🟢 Bugun faollar (${usersData?.activeTodayCount ?? 0})` },
                { id: 'AT_RISK' as const, label: '🔴 Xavf ostidagilar (2+ kun)' },
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setUserFilter(tab.id)}
                  className={`px-4 py-2.5 rounded-xl font-extrabold text-sm sm:text-base transition-all shrink-0 cursor-pointer shadow-sm ${
                    userFilter === tab.id
                      ? 'bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-md shadow-indigo-600/40 ring-2 ring-indigo-400/50'
                      : 'bg-slate-800 text-slate-300 hover:text-white hover:bg-slate-700 border border-slate-700/80'
                  }`}
                >
                  {tab.label}
                </button>
              ))}
            </div>

            {/* O'ngga scroll icon */}
            <button
              type="button"
              onClick={() => scrollFilter('right')}
              className="p-2.5 rounded-xl bg-slate-800/90 hover:bg-slate-700 text-indigo-300 hover:text-white border border-slate-700 shadow-md transition-all shrink-0 cursor-pointer flex items-center justify-center active:scale-95"
              title="O'ngga surish"
            >
              <ChevronRight className="w-5 h-5" />
            </button>
          </div>

          {/* User List */}
          <div className="space-y-3 max-h-[32rem] overflow-y-auto pr-1">
            {filteredUsers.length > 0 ? (
              filteredUsers.map((u) => (
                <div
                  key={u.id}
                  className="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 hover:border-slate-700 transition-all flex flex-col sm:flex-row sm:items-center justify-between gap-3.5 shadow-md"
                >
                  <div className="flex items-center gap-3.5">
                    <div className="w-11 h-11 rounded-2xl bg-gradient-to-br from-indigo-600 to-purple-700 text-white font-black flex items-center justify-center text-base shadow-md shrink-0">
                      {u.firstName.charAt(0).toUpperCase()}
                    </div>
                    <div className="space-y-1">
                      <div className="flex items-center gap-2.5 flex-wrap">
                        <span className="text-base sm:text-lg font-black text-white">{u.firstName}</span>
                        {u.hasUsername && u.username ? (
                          <a
                            href={`https://t.me/${u.username.replace('@', '')}`}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-sm font-mono text-indigo-400 hover:text-indigo-300 flex items-center gap-1 hover:underline font-bold"
                          >
                            <span>{u.username}</span>
                            <ExternalLink className="w-3.5 h-3.5" />
                          </a>
                        ) : (
                          <span className="text-sm font-mono text-slate-400 font-bold">
                            ID: {u.chatId}
                          </span>
                        )}
                        {u.phoneNumber && (
                          <span className="text-xs font-mono text-emerald-400 bg-emerald-500/10 px-2 py-0.5 rounded-lg border border-emerald-500/20 font-bold">
                            📞 {u.phoneNumber}
                          </span>
                        )}
                      </div>
                      
                      {/* User's Target Language, Level & Lesson Progress */}
                      <div className="flex items-center gap-2 text-xs sm:text-sm text-slate-300 flex-wrap font-medium">
                        <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-indigo-500/15 text-indigo-300 border border-indigo-500/30 font-bold">
                          <span>{u.languageFlag}</span>
                          <span>{u.languageName}</span>
                          <span className="text-indigo-400">({u.level})</span>
                        </span>
                        <span>•</span>
                        <span className="text-amber-300 font-bold">
                          📍 {u.currentDayIndex}-kun ({u.currentWordInDay}-so'z)
                        </span>
                        <span>•</span>
                        <span className="font-semibold text-slate-200">{u.wordsLearned} ta so'z</span>
                        <span>•</span>
                        <span className="text-amber-400 font-extrabold">🔥 {u.streak} kun</span>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-row sm:flex-col items-end justify-between sm:justify-center gap-1.5 shrink-0">
                    <span
                      className={`inline-flex items-center px-3 py-1.5 rounded-full text-xs sm:text-sm font-black ${
                        u.statusColor === 'emerald'
                          ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
                          : u.statusColor === 'purple'
                          ? 'bg-purple-500/20 text-purple-300 border border-purple-500/30'
                          : u.statusColor === 'amber'
                          ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
                          : u.statusColor === 'orange'
                          ? 'bg-orange-500/20 text-orange-300 border border-orange-500/30'
                          : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
                      }`}
                    >
                      {u.statusBadge}
                    </span>
                    <span className="text-xs text-slate-400 font-medium">
                      {u.daysSinceLastActive === 0
                        ? 'Bugun faol'
                        : `${u.daysSinceLastActive} kun oldin (${u.lastActiveDate})`}
                    </span>
                  </div>
                </div>
              ))
            ) : (
              <div className="p-8 text-center text-slate-400 text-sm font-medium bg-slate-950/40 rounded-2xl border border-slate-800/60">
                Bu filtr bo'yicha foydalanuvchilar topilmadi.
              </div>
            )}
          </div>
        </div>
      ) : (
        /* Discreet Admin Unlock Button for the owner */
        <div className="flex items-center justify-between p-3 rounded-2xl bg-slate-900/40 border border-slate-800/60 text-xs text-slate-500">
          <div className="flex items-center gap-2">
            <Lock className="w-3.5 h-3.5" />
            <span>Foydalanuvchilar tahlili faqat admin uchun yopiq rejimda.</span>
          </div>
          <button
            onClick={() => setShowPinDialog(true)}
            className="px-3 py-1 rounded-xl bg-slate-800 hover:bg-slate-700 text-indigo-400 hover:text-white font-semibold transition-all cursor-pointer"
          >
            Admin Kirish
          </button>
        </div>
      )}

      {/* Admin Unlock Modal */}
      {showPinDialog && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fadeIn">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-xs w-full space-y-4 shadow-2xl">
            <div className="flex items-center gap-2 text-white font-bold text-sm">
              <ShieldCheck className="w-4 h-4 text-emerald-400" />
              <span>Admin CRM Tasdiqlash</span>
            </div>
            <p className="text-xs text-slate-400">
              Admin parolini kiriting (yoki Telegram bot orqali @MnemonicEngBot ga /start bosing):
            </p>
            <input
              type="password"
              value={adminPinInput}
              onChange={(e) => setAdminPinInput(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleAdminUnlock()}
              placeholder="Admin kodini kiriting..."
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-950 border border-slate-700 text-white text-sm focus:outline-none focus:border-indigo-500"
            />
            <div className="flex items-center justify-end gap-2">
              <button
                onClick={() => setShowPinDialog(false)}
                className="px-3 py-1.5 text-xs text-slate-400 hover:text-white"
              >
                Bekor qilish
              </button>
              <button
                onClick={handleAdminUnlock}
                className="px-4 py-1.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold shadow-md shadow-indigo-600/30"
              >
                Kirish
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 3 Interactive Quick Modules Grid */}
      <div className="grid sm:grid-cols-3 gap-3.5">
        {/* Module 1: Daily 20 Words */}
        <div
          onClick={() => setActiveTab('lesson')}
          className="p-5 rounded-2xl bg-slate-900/70 border border-slate-800 hover:border-indigo-500/50 hover:bg-slate-850 transition-all cursor-pointer group flex flex-col justify-between shadow-md"
        >
          <div className="space-y-2.5">
            <div className="w-11 h-11 rounded-2xl bg-indigo-500/15 border border-indigo-500/25 text-indigo-400 flex items-center justify-center group-hover:scale-105 transition-transform">
              <Sparkles className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-base font-black text-white group-hover:text-indigo-300 transition-colors">
                📅 20 ta So'z Darsi {currentWordIndex > 0 ? `(${currentWordIndex + 1}/20)` : ''}
              </h4>
              <p className="text-xs sm:text-sm text-slate-300 mt-1 font-medium leading-relaxed">
                {currentWordIndex > 0
                  ? `Siz ${currentWordIndex + 1}-so'zdasiz. To'xtagan joyingizdan davom eting.`
                  : '4-bosqichli neyro-metodika: obraz, fonetika, kontekst va audio.'}
              </p>
            </div>
          </div>
          <div className="mt-4 flex items-center gap-1.5 text-xs sm:text-sm font-bold text-indigo-400">
            <span>{currentWordIndex > 0 ? `Davom ettirish (${currentWordIndex + 1}-so'z)` : 'Darsni boshlash'}</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </div>
        </div>

        {/* Module 2: Live Dialogues */}
        <div
          onClick={() => setActiveTab('dialogue')}
          className="p-5 rounded-2xl bg-slate-900/70 border border-slate-800 hover:border-emerald-500/50 hover:bg-slate-850 transition-all cursor-pointer group flex flex-col justify-between shadow-md"
        >
          <div className="space-y-2.5">
            <div className="w-11 h-11 rounded-2xl bg-emerald-500/15 border border-emerald-500/25 text-emerald-400 flex items-center justify-center group-hover:scale-105 transition-transform">
              <MessageSquareQuote className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-base font-black text-white group-hover:text-emerald-300 transition-colors">
                🗣️ Jonli Dialog & Speaking
              </h4>
              <p className="text-xs sm:text-sm text-slate-300 mt-1 font-medium leading-relaxed">
                Supermarket, taksi, restoran va kundalik suhbatlar parallel audio bilan.
              </p>
            </div>
          </div>
          <div className="mt-4 flex items-center gap-1.5 text-xs sm:text-sm font-bold text-emerald-400">
            <span>Dialoglarni tinglash</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </div>
        </div>

        {/* Module 3: Daily Exercises */}
        <div
          onClick={() => setActiveTab('practice')}
          className="p-5 rounded-2xl bg-slate-900/70 border border-slate-800 hover:border-purple-500/50 hover:bg-slate-850 transition-all cursor-pointer group flex flex-col justify-between shadow-md"
        >
          <div className="space-y-2.5">
            <div className="w-11 h-11 rounded-2xl bg-purple-500/15 border border-purple-500/25 text-purple-400 flex items-center justify-center group-hover:scale-105 transition-transform">
              <CheckCircle2 className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-base font-black text-white group-hover:text-purple-300 transition-colors">
                📝 5 ta Mustahkamlovchi Test
              </h4>
              <p className="text-xs sm:text-sm text-slate-300 mt-1 font-medium leading-relaxed">
                Xato qilsangiz mnemonik tushuntirish va audio bilan bilimni mustahkamlang.
              </p>
            </div>
          </div>
          <div className="mt-4 flex items-center gap-1.5 text-xs sm:text-sm font-bold text-purple-400">
            <span>Mashqni boshlash</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </div>
        </div>
      </div>

      {/* Neuro-Memory Hack Card */}
      <div className="p-5 rounded-2xl bg-slate-900/50 border border-slate-800/80 flex items-start gap-4">
        <div className="p-2.5 rounded-2xl bg-amber-500/15 text-amber-400 shrink-0">
          <BrainCircuit className="w-6 h-6" />
        </div>
        <div className="space-y-1">
          <h4 className="text-sm sm:text-base font-black uppercase tracking-wider text-amber-400">
            💡 Poliglot & Neyro-Xotira Qoidasi
          </h4>
          <p className="text-xs sm:text-sm text-slate-200 leading-relaxed font-medium">
            Inson miyasi quruq yodlangan so'zlarning 80% qismini dastlabki 24 soatda unutadi. Ammo har bir so'zga <b>kulgili yoki bo'rttirilgan obraz (Mnemonika)</b> berilsa va talaffuzi ovoz chiqarib 3 marta qaytarilsa — u to'g'ridan-to'g'ri uzoq muddatli xotiraga (LTM) yoziladi!
          </p>
        </div>
      </div>
    </div>
  );
};
