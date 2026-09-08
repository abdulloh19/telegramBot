'use client';

import React, { createContext, useContext, useState, useEffect, useMemo } from 'react';
import { TargetLanguage, WordLevel, AppTab, MnemonicWord, DailyDialogue, UserStats } from '@/types';
import { MNEMONIC_WORDS } from '@/data/words';
import { DAILY_DIALOGUES } from '@/data/dialogues';
import { audioManager } from '@/utils/audio';

interface AppContextType {
  targetLanguage: TargetLanguage;
  setTargetLanguage: (lang: TargetLanguage) => void;
  selectedLevel: WordLevel;
  setSelectedLevel: (level: WordLevel) => void;
  activeTab: AppTab;
  setActiveTab: (tab: AppTab) => void;
  
  // Data filtered by active language & level
  activeWords: MnemonicWord[];
  activeDialogue: DailyDialogue | null;
  currentWordIndex: number;
  setCurrentWordIndex: (idx: number) => void;
  currentWord: MnemonicWord | null;
  
  // Progress & Stats
  stats: UserStats;
  completedWordIds: Set<string>;
  favorites: Set<string>;
  markWordCompleted: (wordId: string) => void;
  toggleFavorite: (wordId: string) => void;
  recordExerciseResult: (correctCount: number, total: number) => void;
  recordQuizScore: (scoreDelta: number) => void;
  addXp: (amount: number) => void;
  
  // Audio helpers
  speakWord: (text: string, onEnd?: () => void) => void;
  isAudioPlaying: boolean;
  setIsAudioPlaying: (playing: boolean) => void;
}

const defaultStats: UserStats = {
  streak: 3,
  maxStreak: 5,
  lastActiveDate: new Date().toISOString().split('T')[0],
  wordsLearnedCount: 18,
  exercisesCompletedCount: 15,
  quizScores: 120,
  xp: 350,
  todayCompleted: false,
};

const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [targetLanguage, setTargetLanguageState] = useState<TargetLanguage>('ru');
  const [selectedLevel, setSelectedLevelState] = useState<WordLevel>('BEGINNER');
  const [activeTab, setActiveTabState] = useState<AppTab>('dashboard');
  const [currentWordIndex, setCurrentWordIndexState] = useState<number>(0);
  const [stats, setStats] = useState<UserStats>(defaultStats);
  const [completedWordIds, setCompletedWordIds] = useState<Set<string>>(new Set(['ru_spasibo', 'ru_vdrug']));
  const [favorites, setFavorites] = useState<Set<string>>(new Set());
  const [isAudioPlaying, setIsAudioPlaying] = useState<boolean>(false);

  // Load persisted state from localStorage on mount
  useEffect(() => {
    try {
      const savedLang = (localStorage.getItem('mnemo_lang') as TargetLanguage) || 'ru';
      if (savedLang === 'en' || savedLang === 'ru') setTargetLanguageState(savedLang);

      const savedLevel = (localStorage.getItem('mnemo_level') as WordLevel) || 'BEGINNER';
      if (savedLevel) setSelectedLevelState(savedLevel);

      const savedStats = localStorage.getItem('mnemo_stats');
      if (savedStats) setStats(JSON.parse(savedStats));

      const savedCompleted = localStorage.getItem('mnemo_completed');
      if (savedCompleted) setCompletedWordIds(new Set(JSON.parse(savedCompleted)));

      const savedFavs = localStorage.getItem('mnemo_favs');
      if (savedFavs) setFavorites(new Set(JSON.parse(savedFavs)));

      // Restore exact word index where user left off
      const savedWordIdx = localStorage.getItem(`mnemo_word_idx_${savedLang}_${savedLevel}`) 
        || localStorage.getItem('mnemo_last_word_idx');
      if (savedWordIdx !== null) {
        const parsed = parseInt(savedWordIdx, 10);
        if (!isNaN(parsed) && parsed >= 0) {
          setCurrentWordIndexState(parsed);
        }
      }

      // Auto-register connected Telegram user dynamically in real-time
      const tgUser = (window as unknown as { Telegram?: { WebApp?: { initDataUnsafe?: { user?: { id?: number; first_name?: string; last_name?: string; username?: string } } } } })
        ?.Telegram?.WebApp?.initDataUnsafe?.user;
      if (tgUser && tgUser.id) {
        localStorage.setItem('mnemo_tg_chat_id', String(tgUser.id));
        fetch('/api/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            chatId: tgUser.id,
            firstName: tgUser.first_name,
            lastName: tgUser.last_name,
            username: tgUser.username,
            language: savedLang,
            level: savedLevel,
          }),
        }).catch(() => {});
      }
    } catch {
      // localStorage may be unavailable
    }
  }, []);

  const setCurrentWordIndex = (idx: number) => {
    setCurrentWordIndexState(idx);
    try {
      localStorage.setItem(`mnemo_word_idx_${targetLanguage}_${selectedLevel}`, String(idx));
      localStorage.setItem('mnemo_last_word_idx', String(idx));

      const savedChatId = localStorage.getItem('mnemo_tg_chat_id');
      const tgUser = (window as unknown as { Telegram?: { WebApp?: { initDataUnsafe?: { user?: { id?: number } } } } })
        ?.Telegram?.WebApp?.initDataUnsafe?.user;
      const currentChatId = tgUser?.id || (savedChatId ? Number(savedChatId) : undefined);

      // Asynchronous background sync to backend users.json
      fetch('/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          chatId: currentChatId,
          wordIndex: idx,
          language: targetLanguage,
          level: selectedLevel,
        }),
      }).catch(() => {});
    } catch {
      // ignore
    }
  };

  const setTargetLanguage = (lang: TargetLanguage) => {
    audioManager.playClickSound();
    setTargetLanguageState(lang);
    try {
      localStorage.setItem('mnemo_lang', lang);
      const savedIdx = localStorage.getItem(`mnemo_word_idx_${lang}_${selectedLevel}`);
      if (savedIdx !== null) {
        const parsed = parseInt(savedIdx, 10);
        setCurrentWordIndexState(!isNaN(parsed) && parsed >= 0 ? parsed : 0);
      } else {
        setCurrentWordIndexState(0);
      }
    } catch {
      setCurrentWordIndexState(0);
    }
  };

  const setSelectedLevel = (level: WordLevel) => {
    audioManager.playClickSound();
    setSelectedLevelState(level);
    try {
      localStorage.setItem('mnemo_level', level);
      const savedIdx = localStorage.getItem(`mnemo_word_idx_${targetLanguage}_${level}`);
      if (savedIdx !== null) {
        const parsed = parseInt(savedIdx, 10);
        setCurrentWordIndexState(!isNaN(parsed) && parsed >= 0 ? parsed : 0);
      } else {
        setCurrentWordIndexState(0);
      }
    } catch {
      setCurrentWordIndexState(0);
    }
  };

  const setActiveTab = (tab: AppTab) => {
    audioManager.playClickSound();
    setActiveTabState(tab);
  };

  // Filter words by language and level
  const activeWords = useMemo(() => {
    const list = MNEMONIC_WORDS.filter(
      w => w.language === targetLanguage && w.level === selectedLevel
    );
    if (list.length === 0) {
      return MNEMONIC_WORDS.filter(w => w.language === targetLanguage);
    }
    return list;
  }, [targetLanguage, selectedLevel]);

  const currentWord = useMemo(() => {
    if (activeWords.length === 0) return null;
    const safeIdx = Math.max(0, Math.min(currentWordIndex, activeWords.length - 1));
    return activeWords[safeIdx];
  }, [activeWords, currentWordIndex]);

  // Filter dialogue by language and level
  const activeDialogue = useMemo(() => {
    const match = DAILY_DIALOGUES.find(
      d => d.language === targetLanguage && d.level === selectedLevel
    );
    return match || DAILY_DIALOGUES.find(d => d.language === targetLanguage) || null;
  }, [targetLanguage, selectedLevel]);

  const markWordCompleted = (wordId: string) => {
    setCompletedWordIds(prev => {
      const next = new Set(prev);
      next.add(wordId);
      try {
        localStorage.setItem('mnemo_completed', JSON.stringify(Array.from(next)));
      } catch {}
      return next;
    });

    setStats(prev => {
      const next = {
        ...prev,
        wordsLearnedCount: prev.wordsLearnedCount + 1,
      };
      try {
        localStorage.setItem('mnemo_stats', JSON.stringify(next));
      } catch {}
      return next;
    });
  };

  const toggleFavorite = (wordId: string) => {
    audioManager.playClickSound();
    setFavorites(prev => {
      const next = new Set(prev);
      if (next.has(wordId)) next.delete(wordId);
      else next.add(wordId);
      try {
        localStorage.setItem('mnemo_favs', JSON.stringify(Array.from(next)));
      } catch {}
      return next;
    });
  };

  const recordExerciseResult = (correctCount: number, total: number) => {
    setStats(prev => {
      const isPerfect = correctCount === total;
      const next: UserStats = {
        ...prev,
        exercisesCompletedCount: prev.exercisesCompletedCount + 1,
        quizScores: prev.quizScores + correctCount * 10,
        streak: isPerfect ? prev.streak + 1 : prev.streak,
        maxStreak: Math.max(prev.maxStreak, isPerfect ? prev.streak + 1 : prev.streak),
        todayCompleted: true,
      };
      try {
        localStorage.setItem('mnemo_stats', JSON.stringify(next));
      } catch {}
      return next;
    });
  };

  const recordQuizScore = (scoreDelta: number) => {
    setStats(prev => {
      const next = {
        ...prev,
        quizScores: Math.max(0, prev.quizScores + scoreDelta),
      };
      try {
        localStorage.setItem('mnemo_stats', JSON.stringify(next));
      } catch {}
      return next;
    });
  };

  const addXp = (amount: number) => {
    setStats(prev => {
      const next = {
        ...prev,
        xp: (prev.xp || 0) + amount,
      };
      try {
        localStorage.setItem('mnemo_stats', JSON.stringify(next));
      } catch {}
      return next;
    });
  };

  const speakWord = (text: string, onEnd?: () => void) => {
    setIsAudioPlaying(true);
    audioManager.speak(text, targetLanguage, () => {
      setIsAudioPlaying(false);
      if (onEnd) onEnd();
    });
  };

  return (
    <AppContext.Provider
      value={{
        targetLanguage,
        setTargetLanguage,
        selectedLevel,
        setSelectedLevel,
        activeTab,
        setActiveTab,
        activeWords,
        activeDialogue,
        currentWordIndex,
        setCurrentWordIndex,
        currentWord,
        stats,
        completedWordIds,
        favorites,
        markWordCompleted,
        toggleFavorite,
        recordExerciseResult,
        recordQuizScore,
        addXp,
        speakWord,
        isAudioPlaying,
        setIsAudioPlaying,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useApp = (): AppContextType => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useApp must be used within an AppProvider');
  }
  return context;
};
