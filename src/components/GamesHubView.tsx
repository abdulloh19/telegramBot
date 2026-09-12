'use client';

import React, { useState, useMemo, useEffect, useRef } from 'react';
import { useApp } from '@/context/AppContext';
import { GameMode, MemoryCardItem, MnemonicWord } from '@/types';
import { GRAMMAR_TOPICS } from '@/data/grammar';
import {
  Layers,
  Sparkles,
  Volume2,
  CheckCircle2,
  XCircle,
  RotateCcw,
  Trophy,
  Award,
  Zap,
  Gamepad2,
  Lightbulb,
  Clock,
  ArrowRight,
  HelpCircle,
  Shuffle,
  ChevronLeft,
  ChevronRight,
  Play,
  Check,
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { audioManager } from '@/utils/audio';

// Russian grammar sorting dataset
const RU_GRAMMAR_SORT_ITEMS = [
  { text: 'Стол', basket: 'М', rule: 'Undosh bilan tugaydi — Мужской род' },
  { text: 'Книга', basket: 'Ж', rule: '-А bilan tugaydi — Женский род' },
  { text: 'Окно', basket: 'С', rule: '-О bilan tugaydi — Средний род' },
  { text: 'Брат', basket: 'М', rule: 'Undosh bilan tugaydi — Мужской род' },
  { text: 'Песня', basket: 'Ж', rule: '-Я bilan tugaydi — Женский род' },
  { text: 'Море', basket: 'С', rule: '-Е bilan tugaydi — Средний род' },
  { text: 'Папа', basket: 'М', rule: 'Istisno: Erkak kishi — Мужской род' },
  { text: 'Солнце', basket: 'С', rule: '-Е bilan tugaydi — Средний род' },
  { text: 'Сестра', basket: 'Ж', rule: '-А bilan tugaydi — Женский род' },
  { text: 'Дом', basket: 'М', rule: 'Undosh bilan tugaydi — Мужской род' },
];

// English grammar sorting dataset (IN / ON / AT pyramid)
const EN_GRAMMAR_SORT_ITEMS = [
  { text: '2026-yil', basket: 'IN', rule: 'Yillar oldidan — IN ishlatiladi (in 2026)' },
  { text: 'Dushanba kuni', basket: 'ON', rule: 'Hafta kunlari oldidan — ON ishlatiladi (on Monday)' },
  { text: 'Soat 9:00 da', basket: 'AT', rule: 'Aniq soat bilan — AT ishlatiladi (at 9:00)' },
  { text: 'Toshkent shahrida', basket: 'IN', rule: 'Shaharlar va davlatlar oldidan — IN (in Tashkent)' },
  { text: 'Tug\'ilgan kunimda', basket: 'ON', rule: 'Maxsus sanalar va kunlar — ON (on my birthday)' },
  { text: 'Yarim tunda', basket: 'AT', rule: 'Aniq nuqta vaqt — AT (at midnight)' },
  { text: 'Yoz faslida', basket: 'IN', rule: 'Fasllar oldidan — IN (in summer)' },
  { text: 'Navoiy ko\'chasida', basket: 'ON', rule: 'Ko\'cha nomlari bilan — ON (on Navoi street)' },
];

export const GamesHubView: React.FC = () => {
  const { targetLanguage, activeWords, speakWord, stats, addXp, setActiveTab } = useApp();
  const [activeGame, setActiveGame] = useState<GameMode>('flashcards');

  // XP & Level calculations
  const currentXp = stats.xp || 350;
  const levelInfo = useMemo(() => {
    if (currentXp < 150) return { level: 1, title: 'Boshlang\'ich O\'rganuvchi', icon: '🥉', next: 150 };
    if (currentXp < 400) return { level: 2, title: 'Xotira Izquvari', icon: '🥈', next: 400 };
    if (currentXp < 800) return { level: 3, title: 'Mnemotexnik Usta', icon: '🥇', next: 800 };
    if (currentXp < 1500) return { level: 4, title: 'So\'z Sehrgari', icon: '💎', next: 1500 };
    return { level: 5, title: 'Poliglot Daho', icon: '👑', next: 3000 };
  }, [currentXp]);

  const levelProgress = Math.min(100, Math.round((currentXp / levelInfo.next) * 100));

  // ==========================================
  // GAME 1: 3D FLASHCARD STUDIO (Quizlet Style)
  // ==========================================
  const [deck, setDeck] = useState<MnemonicWord[]>([]);
  const [cardIndex, setCardIndex] = useState<number>(0);
  const [isFlipped, setIsFlipped] = useState<boolean>(false);
  const [masteredIds, setMasteredIds] = useState<Set<string>>(new Set());
  const [learningIds, setLearningIds] = useState<Set<string>>(new Set());
  const [isDeckFinished, setIsDeckFinished] = useState<boolean>(false);

  // Touch/swipe gesture state for mobile
  const touchStartX = useRef<number>(0);
  const touchEndX = useRef<number>(0);

  const handleTouchStart = (e: React.TouchEvent) => {
    touchStartX.current = e.changedTouches[0].screenX;
  };

  const handleTouchEnd = (e: React.TouchEvent) => {
    touchEndX.current = e.changedTouches[0].screenX;
    const swipeDelta = touchStartX.current - touchEndX.current;
    if (Math.abs(swipeDelta) < 50) return; // Too small — ignore
    if (swipeDelta > 50) handleCardDecision(true);   // Swipe left → mastered
    if (swipeDelta < -50) handleCardDecision(false); // Swipe right → learning
  };

  useEffect(() => {
    if (activeWords && activeWords.length > 0) {
      setDeck([...activeWords]);
      setCardIndex(0);
      setIsFlipped(false);
      setMasteredIds(new Set());
      setLearningIds(new Set());
      setIsDeckFinished(false);
    }
  }, [activeWords, targetLanguage]);

  const currentCardWord = deck[cardIndex] || activeWords[0];

  const handleFlipCard = () => {
    audioManager.playClickSound();
    setIsFlipped(prev => !prev);
  };

  const handleCardDecision = (mastered: boolean) => {
    if (!currentCardWord) return;

    if (mastered) {
      audioManager.playSuccessSound();
      addXp(15);
      setMasteredIds(prev => new Set(prev).add(currentCardWord.id));
      confetti({ particleCount: 25, spread: 50, origin: { y: 0.75 } });
    } else {
      audioManager.playClickSound();
      setLearningIds(prev => new Set(prev).add(currentCardWord.id));
    }

    setIsFlipped(false);
    setTimeout(() => {
      if (cardIndex < deck.length - 1) {
        setCardIndex(prev => prev + 1);
      } else {
        setIsDeckFinished(true);
      }
    }, 250);
  };

  const handlePrevCard = () => {
    if (cardIndex > 0) {
      audioManager.playClickSound();
      setIsFlipped(false);
      setCardIndex(prev => prev - 1);
    }
  };

  const handleNextCardNav = () => {
    if (cardIndex < deck.length - 1) {
      audioManager.playClickSound();
      setIsFlipped(false);
      setCardIndex(prev => prev + 1);
    } else {
      setIsDeckFinished(true);
    }
  };

  const handleShuffleDeck = () => {
    audioManager.playClickSound();
    setIsFlipped(false);
    setDeck(prev => [...prev].sort(() => Math.random() - 0.5));
    setCardIndex(0);
    setIsDeckFinished(false);
  };

  const handleRestartDeck = () => {
    audioManager.playClickSound();
    setIsFlipped(false);
    setCardIndex(0);
    setMasteredIds(new Set());
    setLearningIds(new Set());
    setIsDeckFinished(false);
  };

  useEffect(() => {
    if (activeGame !== 'flashcards' || isDeckFinished) return;
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.code === 'Space') {
        e.preventDefault();
        handleFlipCard();
      } else if (e.code === 'ArrowLeft') {
        e.preventDefault();
        handleCardDecision(false);
      } else if (e.code === 'ArrowRight') {
        e.preventDefault();
        handleCardDecision(true);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [activeGame, isDeckFinished, cardIndex, deck, isFlipped]);

  // ==========================================
  // GAME 2: MEMORY MATCH (Xotira Juftliklari)
  // ==========================================
  const [memoryCards, setMemoryCards] = useState<MemoryCardItem[]>([]);
  const [flippedCards, setFlippedCards] = useState<number[]>([]);
  const [matchTurns, setMatchTurns] = useState<number>(0);
  const [isMatchWon, setIsMatchWon] = useState<boolean>(false);

  const initMemoryGame = () => {
    if (activeWords.length === 0) return;
    const sample = [...activeWords].sort(() => Math.random() - 0.5).slice(0, 6);
    const deck: MemoryCardItem[] = [];

    sample.forEach(w => {
      deck.push({
        id: `${w.id}_tgt`,
        pairId: w.id,
        content: w.word,
        subContent: w.pronunciation,
        type: 'target',
        isFlipped: false,
        isMatched: false,
      });
      deck.push({
        id: `${w.id}_uz`,
        pairId: w.id,
        content: w.uzbekMeaning,
        subContent: `«${w.mnemonicHook}»`,
        type: 'uzbek',
        isFlipped: false,
        isMatched: false,
      });
    });

    setMemoryCards(deck.sort(() => Math.random() - 0.5));
    setFlippedCards([]);
    setMatchTurns(0);
    setIsMatchWon(false);
  };

  useEffect(() => {
    if (activeGame === 'match') {
      initMemoryGame();
    }
  }, [activeGame, activeWords]);

  const handleMemoryCardClick = (index: number) => {
    if (flippedCards.length === 2) return;
    const card = memoryCards[index];
    if (card.isFlipped || card.isMatched) return;

    audioManager.playClickSound();
    const updated = [...memoryCards];
    updated[index].isFlipped = true;
    setMemoryCards(updated);

    const newFlipped = [...flippedCards, index];
    setFlippedCards(newFlipped);

    if (newFlipped.length === 2) {
      setMatchTurns(t => t + 1);
      const [firstIdx, secondIdx] = newFlipped;
      const firstCard = updated[firstIdx];
      const secondCard = updated[secondIdx];

      if (firstCard.pairId === secondCard.pairId) {
        audioManager.playSuccessSound();
        addXp(15);
        setTimeout(() => {
          setMemoryCards(prev =>
            prev.map((c, i) =>
              i === firstIdx || i === secondIdx ? { ...c, isMatched: true } : c
            )
          );
          setFlippedCards([]);

          // Check all matched
          const remaining = updated.filter((c, i) => i !== firstIdx && i !== secondIdx && !c.isMatched);
          if (remaining.length === 0) {
            setIsMatchWon(true);
            addXp(50);
            confetti({ particleCount: 80, spread: 70, origin: { y: 0.6 } });
          }
        }, 500);
      } else {
        audioManager.playErrorSound();
        setTimeout(() => {
          setMemoryCards(prev =>
            prev.map((c, i) =>
              i === firstIdx || i === secondIdx ? { ...c, isFlipped: false } : c
            )
          );
          setFlippedCards([]);
        }, 900);
      }
    }
  };

  // ==========================================
  // GAME 3: WORD SCRAMBLE (Harflardan So'z)
  // ==========================================
  const [scrambleIndex, setScrambleIndex] = useState<number>(0);
  const currentScrambleWord = activeWords[scrambleIndex] || activeWords[0];
  const [scrambledLetters, setScrambledLetters] = useState<{ id: number; char: string; used: boolean }[]>([]);
  const [assembledLetters, setAssembledLetters] = useState<{ id: number; char: string }[]>([]);
  const [isScrambleCorrect, setIsScrambleCorrect] = useState<boolean | null>(null);

  useEffect(() => {
    if (!currentScrambleWord) return;
    const cleanWord = currentScrambleWord.word.toUpperCase().replace(/[^A-ZА-ЯЁ]/g, '');
    const letters = cleanWord.split('').map((char, id) => ({ id, char, used: false }));
    // Shuffle letters ensuring not exact match
    setScrambledLetters(letters.sort(() => Math.random() - 0.5));
    setAssembledLetters([]);
    setIsScrambleCorrect(null);
  }, [scrambleIndex, currentScrambleWord]);

  const handlePickLetter = (item: { id: number; char: string; used: boolean }) => {
    if (item.used || isScrambleCorrect) return;
    audioManager.playClickSound();

    setScrambledLetters(prev =>
      prev.map(l => (l.id === item.id ? { ...l, used: true } : l))
    );
    const newAssembled = [...assembledLetters, { id: item.id, char: item.char }];
    setAssembledLetters(newAssembled);

    // If assembled length equals word length, verify
    const cleanWord = currentScrambleWord.word.toUpperCase().replace(/[^A-ZА-ЯЁ]/g, '');
    if (newAssembled.length === cleanWord.length) {
      const assembledText = newAssembled.map(a => a.char).join('');
      if (assembledText === cleanWord) {
        setIsScrambleCorrect(true);
        audioManager.playSuccessSound();
        addXp(20);
        confetti({ particleCount: 30, spread: 45, origin: { y: 0.7 } });
        setTimeout(() => {
          setScrambleIndex(prev => (prev + 1) % activeWords.length);
        }, 1200);
      } else {
        setIsScrambleCorrect(false);
        audioManager.playErrorSound();
      }
    }
  };

  const handleResetScramble = () => {
    audioManager.playClickSound();
    setScrambledLetters(prev => prev.map(l => ({ ...l, used: false })));
    setAssembledLetters([]);
    setIsScrambleCorrect(null);
  };

  const handleRemoveAssembledLetter = (assembledIndex: number) => {
    if (isScrambleCorrect === true) return;
    audioManager.playClickSound();
    const removed = assembledLetters[assembledIndex];
    setAssembledLetters(prev => prev.filter((_, i) => i !== assembledIndex));
    setScrambledLetters(prev =>
      prev.map(l => (l.id === removed.id ? { ...l, used: false } : l))
    );
    setIsScrambleCorrect(null);
  };

  // ==========================================
  // GAME 4: GRAMMAR SORT (Grammatika Saralash)
  // ==========================================
  const grammarSortPool = targetLanguage === 'ru' ? RU_GRAMMAR_SORT_ITEMS : EN_GRAMMAR_SORT_ITEMS;
  const [grammarSortIndex, setGrammarSortIndex] = useState<number>(0);
  const [grammarFeedback, setGrammarFeedback] = useState<{ isCorrect: boolean; rule: string } | null>(null);
  const currentGrammarItem = grammarSortPool[grammarSortIndex] || grammarSortPool[0];

  const handleBasketSort = (selectedBasket: string) => {
    if (grammarFeedback) return;
    const isCorrect = selectedBasket === currentGrammarItem.basket;

    if (isCorrect) {
      audioManager.playSuccessSound();
      addXp(15);
      confetti({ particleCount: 25, spread: 40, origin: { y: 0.7 } });
      setGrammarFeedback({ isCorrect: true, rule: currentGrammarItem.rule });
      setTimeout(() => {
        setGrammarFeedback(null);
        setGrammarSortIndex(prev => (prev + 1) % grammarSortPool.length);
      }, 1100);
    } else {
      audioManager.playErrorSound();
      setGrammarFeedback({ isCorrect: false, rule: currentGrammarItem.rule });
    }
  };

  const handleNextGrammar = () => {
    audioManager.playClickSound();
    setGrammarFeedback(null);
    setGrammarSortIndex(prev => (prev + 1) % grammarSortPool.length);
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6 pb-28 animate-fade-in">
      
      {/* Gamification Level & XP Banner */}
      <div className="p-4 sm:p-5 rounded-3xl bg-slate-900/90 border border-slate-800 backdrop-blur-xl shadow-xl space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2.5">
            <span className="text-2xl">{levelInfo.icon}</span>
            <div>
              <div className="text-xs font-semibold text-slate-400">
                {levelInfo.title}
              </div>
              <div className="text-base font-black text-white">
                Daraja {levelInfo.level}
              </div>
            </div>
          </div>

          <div className="text-right">
            <div className="text-xs font-mono font-bold text-amber-400">
              ⚡ {currentXp} XP
            </div>
            <div className="text-[10px] text-slate-400 font-mono">
              Keyingi: {levelInfo.next} XP
            </div>
          </div>
        </div>

        {/* Progress bar */}
        <div className="w-full h-2 bg-slate-950 rounded-full overflow-hidden p-0.5 border border-slate-800">
          <div
            className="h-full bg-gradient-to-r from-amber-500 via-purple-500 to-indigo-500 rounded-full transition-all duration-500"
            style={{ width: `${levelProgress}%` }}
          />
        </div>
      </div>

      {/* Game Mode Selector Nav */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-2">
        <button
          id="btn-game-flashcards"
          onClick={() => { audioManager.playClickSound(); setActiveGame('flashcards'); }}
          className={`p-3 rounded-2xl border text-xs font-bold transition-all flex flex-col items-center justify-center gap-1.5 cursor-pointer ${
            activeGame === 'flashcards'
              ? 'bg-gradient-to-tr from-indigo-600 to-purple-600 text-white border-indigo-500 shadow-lg shadow-indigo-500/25'
              : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white hover:bg-slate-800/60'
          }`}
        >
          <span className="text-lg">🎴</span>
          <span>3D Kartochkalar</span>
        </button>

        <button
          id="btn-game-match"
          onClick={() => { audioManager.playClickSound(); setActiveGame('match'); }}
          className={`p-3 rounded-2xl border text-xs font-bold transition-all flex flex-col items-center justify-center gap-1.5 cursor-pointer ${
            activeGame === 'match'
              ? 'bg-gradient-to-tr from-emerald-600 to-teal-600 text-white border-emerald-500 shadow-lg shadow-emerald-500/25'
              : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white hover:bg-slate-800/60'
          }`}
        >
          <span className="text-lg">🧠</span>
          <span>Xotira Jufti</span>
        </button>

        <button
          id="btn-game-scramble"
          onClick={() => { audioManager.playClickSound(); setActiveGame('scramble'); }}
          className={`p-3 rounded-2xl border text-xs font-bold transition-all flex flex-col items-center justify-center gap-1.5 cursor-pointer ${
            activeGame === 'scramble'
              ? 'bg-gradient-to-tr from-amber-600 to-orange-600 text-white border-amber-500 shadow-lg shadow-amber-500/25'
              : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white hover:bg-slate-800/60'
          }`}
        >
          <span className="text-lg">🧩</span>
          <span>So'zni Terish</span>
        </button>

        <button
          id="btn-game-sort"
          onClick={() => { audioManager.playClickSound(); setActiveGame('grammarSort'); }}
          className={`p-3 rounded-2xl border text-xs font-bold transition-all flex flex-col items-center justify-center gap-1.5 cursor-pointer ${
            activeGame === 'grammarSort'
              ? 'bg-gradient-to-tr from-pink-600 to-rose-600 text-white border-pink-500 shadow-lg shadow-pink-500/25'
              : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white hover:bg-slate-800/60'
          }`}
        >
          <span className="text-lg">📐</span>
          <span>Grammatika Saralash</span>
        </button>
      </div>

      {/* ==================================================== */}
      {/* MODE 1: 3D FLASHCARD STUDIO (Quizlet Style) */}
      {/* ==================================================== */}
      {activeGame === 'flashcards' && (
        <div className="space-y-4 animate-fade-in">
          {isDeckFinished ? (
            // Quizlet-style Deck Completion Card
            <div className="p-7 sm:p-9 rounded-3xl bg-gradient-to-br from-slate-900 via-indigo-950/40 to-slate-900 border border-indigo-500/40 shadow-2xl text-center space-y-6 animate-fade-in">
              <div className="w-16 h-16 mx-auto rounded-3xl bg-amber-500/20 text-amber-400 border border-amber-500/30 flex items-center justify-center text-3xl shadow-lg shadow-amber-500/20">
                🏆
              </div>

              <div className="space-y-2">
                <h3 className="text-2xl sm:text-3xl font-black text-white">
                  Ajoyib! Barcha kartochkalarni ko'rib chiqdingiz!
                </h3>
                <p className="text-sm text-slate-300 max-w-md mx-auto">
                  Mnemotexnik assotsiatsiyalar xotirangizda mustahkamlandi. Endi bilimlaringizni test orqali sinab ko'ring!
                </p>
              </div>

              <div className="grid grid-cols-2 gap-3.5 max-w-xs mx-auto">
                <div className="p-4 rounded-2xl bg-emerald-950/40 border border-emerald-500/40 text-center">
                  <div className="text-3xl font-black text-emerald-400 font-mono">
                    {masteredIds.size}
                  </div>
                  <div className="text-xs font-bold text-emerald-300 mt-1">
                    Yodda qoldi
                  </div>
                </div>

                <div className="p-4 rounded-2xl bg-amber-950/40 border border-amber-500/40 text-center">
                  <div className="text-3xl font-black text-amber-400 font-mono">
                    {learningIds.size}
                  </div>
                  <div className="text-xs font-bold text-amber-300 mt-1">
                    Takrorlash kerak
                  </div>
                </div>
              </div>

              <div className="flex flex-col sm:flex-row items-center justify-center gap-3 pt-2">
                <button
                  onClick={() => setActiveTab('quiz')}
                  className="w-full sm:w-auto px-6 py-3.5 rounded-2xl bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 hover:from-indigo-500 hover:to-pink-500 text-white font-extrabold text-sm shadow-xl shadow-indigo-600/30 transition-all flex items-center justify-center gap-2 cursor-pointer active:scale-95"
                >
                  <Trophy className="w-4 h-4" />
                  <span>🎯 Viktorinada (Quiz) Sinash</span>
                  <ArrowRight className="w-4 h-4" />
                </button>

                <button
                  onClick={handleRestartDeck}
                  className="w-full sm:w-auto px-5 py-3.5 rounded-2xl bg-slate-800 hover:bg-slate-700 text-slate-200 hover:text-white font-bold text-xs border border-slate-700 transition-all flex items-center justify-center gap-2 cursor-pointer"
                >
                  <RotateCcw className="w-4 h-4 text-amber-400" />
                  <span>Qayta boshlash</span>
                </button>

                <button
                  onClick={handleShuffleDeck}
                  className="w-full sm:w-auto px-5 py-3.5 rounded-2xl bg-slate-800 hover:bg-slate-700 text-slate-200 hover:text-white font-bold text-xs border border-slate-700 transition-all flex items-center justify-center gap-2 cursor-pointer"
                >
                  <Shuffle className="w-4 h-4 text-indigo-400" />
                  <span>Aralashtirish</span>
                </button>
              </div>
            </div>
          ) : (
            currentCardWord && (
              <>
                {/* Quizlet-style Progress Bar & Controls */}
                <div className="p-3.5 rounded-2xl bg-slate-900/80 border border-slate-800 space-y-2.5">
                  <div className="flex items-center justify-between text-xs">
                    <div className="flex items-center gap-3 font-semibold">
                      <span className="flex items-center gap-1.5 text-emerald-400">
                        <Check className="w-3.5 h-3.5" />
                        <span>{masteredIds.size} yodda qoldi</span>
                      </span>
                      <span className="text-slate-600">•</span>
                      <span className="flex items-center gap-1.5 text-amber-400">
                        <RotateCcw className="w-3.5 h-3.5" />
                        <span>{learningIds.size} takrorlash</span>
                      </span>
                    </div>

                    <div className="flex items-center gap-2">
                      <button
                        onClick={handleShuffleDeck}
                        className="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white transition-all cursor-pointer"
                        title="Kartalarni aralashtirish"
                      >
                        <Shuffle className="w-3.5 h-3.5" />
                      </button>
                      <button
                        onClick={handleRestartDeck}
                        className="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white transition-all cursor-pointer"
                        title="Boshidan qayta boshlash"
                      >
                        <RotateCcw className="w-3.5 h-3.5" />
                      </button>
                      <span className="font-mono text-indigo-400 font-bold px-2 py-0.5 rounded-md bg-indigo-500/15 border border-indigo-500/30">
                        {cardIndex + 1} / {deck.length}
                      </span>
                    </div>
                  </div>

                  {/* Horizontal Segmented Progress Bar */}
                  <div className="w-full h-2 rounded-full bg-slate-800 overflow-hidden flex">
                    <div
                      className="h-full bg-emerald-500 transition-all duration-300"
                      style={{ width: `${(masteredIds.size / (deck.length || 1)) * 100}%` }}
                    />
                    <div
                      className="h-full bg-amber-500 transition-all duration-300"
                      style={{ width: `${(learningIds.size / (deck.length || 1)) * 100}%` }}
                    />
                  </div>
                </div>

                {/* 3D Physical Flip Card Container (Quizlet Style) */}
                <div
                  onClick={handleFlipCard}
                  onTouchStart={handleTouchStart}
                  onTouchEnd={handleTouchEnd}
                  className="relative w-full cursor-pointer select-none group"
                  style={{ perspective: '1200px', minHeight: '310px' }}
                >
                  <div
                    className="relative w-full rounded-3xl transition-transform duration-500"
                    style={{
                      transformStyle: 'preserve-3d',
                      transform: isFlipped ? 'rotateY(180deg)' : 'rotateY(0deg)',
                      minHeight: '310px',
                    }}
                  >
                    {/* FRONT SIDE (Original Term / Word) */}
                    <div
                      className="absolute inset-0 p-6 sm:p-8 rounded-3xl bg-gradient-to-br from-slate-900 via-slate-900/95 to-indigo-950/70 border border-indigo-500/40 shadow-2xl backdrop-blur-xl flex flex-col justify-between"
                      style={{ backfaceVisibility: 'hidden', WebkitBackfaceVisibility: 'hidden' }}
                    >
                      <div className="flex items-center justify-between text-xs text-slate-400 font-bold">
                        <span className="px-3 py-1 rounded-full bg-indigo-500/20 text-indigo-300 border border-indigo-500/30 uppercase tracking-wider text-[10px]">
                          {targetLanguage === 'ru' ? '🇷🇺 Ruscha So\'z' : '🇬🇧 Inglizcha So\'z'}
                        </span>
                        <span className="text-[11px] text-slate-400">
                          3D Kartochka (Quizlet)
                        </span>
                      </div>

                      <div className="space-y-2 text-center my-auto">
                        <div className="text-3xl sm:text-4xl lg:text-5xl font-black text-white tracking-tight uppercase">
                          {currentCardWord.word}
                        </div>
                        <div className="text-base sm:text-lg font-mono text-indigo-400 font-semibold">
                          {currentCardWord.pronunciation}
                        </div>

                        <div className="pt-3 flex justify-center">
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              speakWord(currentCardWord.word);
                            }}
                            className="p-3.5 rounded-2xl bg-indigo-600/20 text-indigo-300 hover:bg-indigo-600 hover:text-white transition-all cursor-pointer shadow-lg shadow-indigo-500/25 active:scale-95"
                            title="Talaffuzni tinglash"
                          >
                            <Volume2 className="w-5 h-5" />
                          </button>
                        </div>
                      </div>

                      <div className="text-[11px] text-slate-400 flex items-center justify-center gap-1.5 pt-2 border-t border-slate-800/80">
                        <RotateCcw className="w-3.5 h-3.5 text-indigo-400" />
                        <span>Ma'nosi va mnemonikani ko'rish uchun kartaga bosing (yoki Probel)</span>
                      </div>
                    </div>

                    {/* BACK SIDE (Uzbek Translation & Mnemonic Hook) */}
                    <div
                      className="absolute inset-0 p-6 sm:p-7 rounded-3xl bg-gradient-to-br from-slate-900 via-slate-900/95 to-emerald-950/70 border border-emerald-500/40 shadow-2xl backdrop-blur-xl flex flex-col justify-between"
                      style={{
                        backfaceVisibility: 'hidden',
                        WebkitBackfaceVisibility: 'hidden',
                        transform: 'rotateY(180deg)',
                      }}
                    >
                      <div className="flex items-center justify-between text-xs font-bold">
                        <span className="px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 uppercase tracking-wider text-[10px]">
                          🇺🇿 O'zbekcha Ma'nosi
                        </span>
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            speakWord(currentCardWord.word);
                          }}
                          className="p-1.5 rounded-xl bg-emerald-600/20 text-emerald-300 hover:bg-emerald-600 hover:text-white transition-all"
                        >
                          <Volume2 className="w-4 h-4" />
                        </button>
                      </div>

                      <div className="space-y-3.5 my-auto">
                        <div className="text-center">
                          <div className="text-2xl sm:text-3xl font-black text-white">
                            {currentCardWord.uzbekMeaning}
                          </div>
                        </div>

                        {/* Mnemonic Hook Banner */}
                        <div className="p-3.5 rounded-2xl bg-amber-500/15 border border-amber-500/30 space-y-1">
                          <div className="flex items-center gap-1.5 text-xs font-bold text-amber-400">
                            <Zap className="w-4 h-4" />
                            <span>Mnemonik Ilmoq: «{currentCardWord.mnemonicHook}»</span>
                          </div>
                          <p className="text-xs text-slate-200 italic leading-relaxed font-medium">
                            {currentCardWord.mnemonicStory}
                          </p>
                        </div>

                        {/* Example Sentence */}
                        <div className="text-xs text-slate-300 bg-slate-950/60 p-3 rounded-xl border border-slate-800 space-y-1">
                          <div className="font-semibold text-white">
                            {currentCardWord.exampleTarget}
                          </div>
                          <div className="text-slate-400 italic">
                            — {currentCardWord.exampleUz}
                          </div>
                        </div>
                      </div>

                      <div className="text-[11px] text-slate-400 flex items-center justify-center gap-1.5 pt-2 border-t border-slate-800/80">
                        <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />
                        <span>Karta orqasini o'rgandingiz. Baholang va keyingisiga o'ting!</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Quizlet Response Actions & Nav Bar */}
                <div className="space-y-2.5 pt-1">
                  <div className="grid grid-cols-2 gap-3">
                    <button
                      onClick={() => handleCardDecision(false)}
                      className="py-3.5 px-4 rounded-2xl bg-slate-900 hover:bg-slate-800 text-amber-300 font-bold text-xs border border-amber-500/30 transition-all flex items-center justify-center gap-2 cursor-pointer active:scale-95 shadow-lg shadow-amber-500/10"
                    >
                      <RotateCcw className="w-4 h-4 text-amber-400" />
                      <span>Takrorlash kerak</span>
                    </button>

                    <button
                      onClick={() => handleCardDecision(true)}
                      className="py-3.5 px-4 rounded-2xl bg-gradient-to-r from-emerald-600 via-teal-600 to-emerald-600 hover:from-emerald-500 hover:to-teal-500 text-white font-black text-xs shadow-xl shadow-emerald-500/30 transition-all flex items-center justify-center gap-2 cursor-pointer active:scale-95"
                    >
                      <CheckCircle2 className="w-4 h-4" />
                      <span>Bilaman! (+15 XP)</span>
                    </button>
                  </div>

                  {/* Navigation Helper Buttons */}
                  <div className="flex items-center justify-between px-2 text-xs text-slate-400">
                    <button
                      onClick={handlePrevCard}
                      disabled={cardIndex === 0}
                      className="flex items-center gap-1 hover:text-white disabled:opacity-30 disabled:cursor-not-allowed cursor-pointer font-medium"
                    >
                      <ChevronLeft className="w-4 h-4" />
                      <span>Oldingi karta</span>
                    </button>

                    <button
                      onClick={handleFlipCard}
                      className="px-3 py-1 rounded-lg bg-slate-800/80 hover:bg-slate-700 text-slate-300 hover:text-white cursor-pointer font-medium"
                    >
                      Aylantirish (Space)
                    </button>

                    <button
                      onClick={handleNextCardNav}
                      className="flex items-center gap-1 hover:text-white cursor-pointer font-medium"
                    >
                      <span>Keyingi karta</span>
                      <ChevronRight className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </>
            )
          )}
        </div>
      )}

      {/* ==================================================== */}
      {/* MODE 2: XOTIRA JUFTLIKLARI (MEMORY MATCH) */}
      {/* ==================================================== */}
      {activeGame === 'match' && (
        <div className="space-y-4 animate-fade-in">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>🧠 Xotira Juftliklari (Kartalarni oching)</span>
            <div className="flex items-center gap-3 font-mono">
              <span>Urinishlar: <b className="text-indigo-400">{matchTurns}</b></span>
              <button
                onClick={initMemoryGame}
                className="text-xs text-slate-400 hover:text-white flex items-center gap-1 cursor-pointer"
              >
                <RotateCcw className="w-3.5 h-3.5" />
                <span>Qayta</span>
              </button>
            </div>
          </div>

          {/* 4x3 Cards Grid */}
          <div className="grid grid-cols-3 sm:grid-cols-4 gap-2.5">
            {memoryCards.map((card, idx) => {
              const showContent = card.isFlipped || card.isMatched;

              return (
                <button
                  key={card.id}
                  disabled={showContent}
                  onClick={() => handleMemoryCardClick(idx)}
                  className={`h-24 sm:h-28 rounded-2xl border p-2.5 flex flex-col items-center justify-center text-center transition-all duration-300 cursor-pointer ${
                    card.isMatched
                      ? 'bg-emerald-950/60 border-emerald-500 text-emerald-300 shadow-md shadow-emerald-500/20 opacity-80'
                      : showContent
                      ? 'bg-slate-800 border-indigo-500 text-white shadow-lg shadow-indigo-500/20'
                      : 'bg-slate-900/90 border-slate-800 hover:border-slate-700 text-slate-500 hover:bg-slate-850'
                  }`}
                >
                  {showContent ? (
                    <div className="space-y-1 animate-fade-in">
                      <div className="text-xs sm:text-sm font-black leading-tight line-clamp-2">
                        {card.content}
                      </div>
                      {card.subContent && (
                        <div className="text-[10px] font-mono opacity-70 line-clamp-1">
                          {card.subContent}
                        </div>
                      )}
                    </div>
                  ) : (
                    <span className="text-2xl opacity-40 group-hover:scale-110 transition-transform">
                      ❓
                    </span>
                  )}
                </button>
              );
            })}
          </div>

          {/* Win Modal Banner */}
          {isMatchWon && (
            <div className="p-6 rounded-3xl bg-gradient-to-r from-emerald-950/80 to-slate-900 border border-emerald-500/40 text-center space-y-3 animate-fade-in shadow-2xl">
              <Trophy className="w-10 h-10 text-emerald-400 mx-auto animate-bounce" />
              <div className="space-y-1">
                <h3 className="text-xl font-black text-white">Tabriklaymiz! Barcha juftliklar topildi!</h3>
                <p className="text-xs text-slate-300">
                  Siz {matchTurns} urinishda barcha 6 ta so'z juftligini eslab qoldingiz.
                </p>
              </div>
              <div className="text-sm font-mono font-bold text-amber-400">+50 XP yutib oldingiz! 🎉</div>
              <button
                onClick={initMemoryGame}
                className="px-6 py-2.5 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 text-white text-xs font-bold shadow-lg cursor-pointer"
              >
                Yana o'ynash 🔄
              </button>
            </div>
          )}
        </div>
      )}

      {/* ==================================================== */}
      {/* MODE 3: WORD SCRAMBLE (Harflardan So'z) */}
      {/* ==================================================== */}
      {activeGame === 'scramble' && currentScrambleWord && (
        <div className="p-6 sm:p-8 rounded-3xl bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-xl space-y-6 animate-fade-in">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>🧩 Harflardan So'zni Tiklang</span>
            <span className="font-mono text-indigo-400 font-bold">
              {scrambleIndex + 1} / {activeWords.length}
            </span>
          </div>

          {/* Clue Prompt */}
          <div className="text-center space-y-2">
            <div className="text-xs text-slate-400 font-medium">
              Ma'nosi:
            </div>
            <div className="text-2xl font-black text-white">
              {currentScrambleWord.uzbekMeaning}
            </div>
            <div className="text-xs text-amber-300 italic bg-amber-500/10 py-1.5 px-3 rounded-full inline-block border border-amber-500/20">
              💡 Ilmoq: «{currentScrambleWord.mnemonicHook}»
            </div>
          </div>

          {/* Assembled Letters Slots - Clickable to remove */}
          <div className="flex flex-wrap justify-center gap-2.5 min-h-[56px] p-3.5 rounded-2xl bg-slate-950/80 border border-slate-800">
            {assembledLetters.map((item, idx) => (
              <button
                key={idx}
                type="button"
                onClick={() => handleRemoveAssembledLetter(idx)}
                className={`w-11 h-11 sm:w-12 sm:h-12 rounded-xl flex items-center justify-center font-black text-lg sm:text-xl border shadow-md transition-all cursor-pointer hover:scale-105 active:scale-95 animate-fade-in ${
                  isScrambleCorrect === true
                    ? 'bg-emerald-500 text-slate-950 border-emerald-400'
                    : isScrambleCorrect === false
                    ? 'bg-rose-500/25 text-rose-300 border-rose-500 hover:bg-rose-500/40 ring-2 ring-rose-500/40'
                    : 'bg-indigo-600 text-white border-indigo-400 hover:bg-indigo-500'
                }`}
                title="Harfni qaytarib olish uchun bosing"
              >
                {item.char}
              </button>
            ))}
            {assembledLetters.length === 0 && (
              <span className="text-xs sm:text-sm text-slate-400 my-auto font-medium">
                Harflarni bosib so'zni to'g'ri yig'ing...
              </span>
            )}
          </div>

          {/* Explanation banner on error */}
          {isScrambleCorrect === false && (
            <div className="p-4 sm:p-5 rounded-2xl bg-rose-950/40 border border-rose-500/40 space-y-2.5 animate-fade-in text-left">
              <div className="flex items-center justify-between gap-2 flex-wrap">
                <div className="flex items-center gap-2 text-rose-400 font-extrabold text-sm sm:text-base">
                  <XCircle className="w-5 h-5 shrink-0" />
                  <span>
                    Xatolik: Siz «{assembledLetters.map(a => a.char).join('')}» deb yozdingiz
                  </span>
                </div>
                <button
                  onClick={handleResetScramble}
                  className="px-3.5 py-1.5 rounded-xl bg-rose-600 hover:bg-rose-500 text-white font-bold text-xs cursor-pointer shadow-md transition-all"
                >
                  Qaytadan to'plash 🔄
                </button>
              </div>

              <div className="text-xs sm:text-sm text-slate-200 leading-relaxed space-y-1.5">
                <p className="font-semibold">
                  To'g'ri yozilishi:{' '}
                  <span className="font-mono font-black text-emerald-400 text-base sm:text-lg tracking-wider">
                    {currentScrambleWord.word.toUpperCase()}
                  </span>
                </p>
                {currentScrambleWord.mnemonicHook && (
                  <div className="p-2.5 rounded-xl bg-amber-500/15 border border-amber-500/30 text-amber-300 font-medium">
                    💡 <b>Neyro-ilmoq:</b> «{currentScrambleWord.mnemonicHook}» ({currentScrambleWord.mnemonicStory})
                  </div>
                )}
                {targetLanguage === 'ru' && currentScrambleWord.word.toUpperCase() === 'СПАСИБО' && (
                  <p className="text-slate-300 italic text-xs leading-relaxed">
                    Eslatma: Rus tilida aytilishida urg'usiz «О» harfi [А] deb eshitiladi (shuning uchun «спасиб[а]» deb talaffuz qilinadi), ammo imlosida doimo <b>«СПАСИБО»</b> yoziladi (3-harf: <b>«А»</b>, oxirgi harf: <b>«О»</b>).
                  </p>
                )}
              </div>
            </div>
          )}

          {/* Scrambled Letter Tiles */}
          <div className="flex flex-wrap justify-center gap-2.5">
            {scrambledLetters.map((l) => (
              <button
                key={l.id}
                disabled={l.used || isScrambleCorrect === true}
                onClick={() => handlePickLetter(l)}
                className={`w-12 h-12 rounded-2xl text-base sm:text-lg font-black border transition-all cursor-pointer ${
                  l.used
                    ? 'bg-slate-900 border-slate-850 text-slate-600 opacity-20 scale-90'
                    : 'bg-slate-800 border-slate-700 text-white hover:bg-indigo-600 hover:border-indigo-400 hover:scale-105 active:scale-95 shadow-md'
                }`}
              >
                {l.char}
              </button>
            ))}
          </div>

          {/* Reset button */}
          <div className="flex justify-between items-center pt-2">
            <button
              onClick={handleResetScramble}
              className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-slate-800 text-slate-300 text-xs sm:text-sm font-bold hover:bg-slate-700 cursor-pointer transition-all"
            >
              <RotateCcw className="w-4 h-4" />
              <span>Tozalash</span>
            </button>

            <button
              onClick={() => speakWord(currentScrambleWord.word)}
              className="p-2.5 rounded-xl bg-indigo-500/20 text-indigo-300 hover:bg-indigo-500 hover:text-white cursor-pointer transition-all"
              title="Talaffuz"
            >
              <Volume2 className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}

      {/* ==================================================== */}
      {/* MODE 4: GRAMMATIKA SARALASH (GRAMMAR SORT) */}
      {/* ==================================================== */}
      {activeGame === 'grammarSort' && currentGrammarItem && (
        <div className="p-6 sm:p-8 rounded-3xl bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-xl space-y-6 animate-fade-in">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>
              {targetLanguage === 'ru' ? '🇷🇺 Ruscha Otlar Rodini Saralang' : '🇬🇧 IN / ON / AT Predloglarini Saralang'}
            </span>
            <span className="font-mono text-indigo-400 font-bold">
              {grammarSortIndex + 1} / {grammarSortPool.length}
            </span>
          </div>

          {/* Item to classify */}
          <div className="p-6 rounded-2xl bg-slate-950/80 border border-slate-800 text-center space-y-1">
            <div className="text-xs text-slate-400 uppercase tracking-widest font-semibold">
              Qaysi toifaga kiradi?
            </div>
            <div className="text-3xl font-black text-white tracking-tight">
              {currentGrammarItem.text}
            </div>
          </div>

          {/* Russian Baskets: M, Ж, C */}
          {targetLanguage === 'ru' ? (
            <div className="grid grid-cols-3 gap-2.5">
              <button
                onClick={() => handleBasketSort('М')}
                className="py-4 rounded-2xl bg-gradient-to-b from-blue-950/40 to-slate-900 border border-blue-500/40 hover:border-blue-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-blue-500/10"
              >
                <span className="text-lg">👨</span>
                <span>Мужской (Он)</span>
              </button>

              <button
                onClick={() => handleBasketSort('Ж')}
                className="py-4 rounded-2xl bg-gradient-to-b from-pink-950/40 to-slate-900 border border-pink-500/40 hover:border-pink-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-pink-500/10"
              >
                <span className="text-lg">👩</span>
                <span>Женский (Она)</span>
              </button>

              <button
                onClick={() => handleBasketSort('С')}
                className="py-4 rounded-2xl bg-gradient-to-b from-purple-950/40 to-slate-900 border border-purple-500/40 hover:border-purple-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-purple-500/10"
              >
                <span className="text-lg">📦</span>
                <span>Средний (Оно)</span>
              </button>
            </div>
          ) : (
            // English Baskets: IN, ON, AT
            <div className="grid grid-cols-3 gap-2.5">
              <button
                onClick={() => handleBasketSort('IN')}
                className="py-4 rounded-2xl bg-gradient-to-b from-indigo-950/40 to-slate-900 border border-indigo-500/40 hover:border-indigo-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-indigo-500/10"
              >
                <span className="text-lg">🔺</span>
                <span>IN (Keng)</span>
              </button>

              <button
                onClick={() => handleBasketSort('ON')}
                className="py-4 rounded-2xl bg-gradient-to-b from-teal-950/40 to-slate-900 border border-teal-500/40 hover:border-teal-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-teal-500/10"
              >
                <span className="text-lg">📅</span>
                <span>ON (Kun/Ko'cha)</span>
              </button>

              <button
                onClick={() => handleBasketSort('AT')}
                className="py-4 rounded-2xl bg-gradient-to-b from-amber-950/40 to-slate-900 border border-amber-500/40 hover:border-amber-400 text-white font-bold text-xs sm:text-sm flex flex-col items-center justify-center gap-1 cursor-pointer transition-all active:scale-95 shadow-lg shadow-amber-500/10"
              >
                <span className="text-lg">🎯</span>
                <span>AT (Nuqta/Soat)</span>
              </button>
            </div>
          )}

          {/* Feedback & Rule Box */}
          {grammarFeedback && (
            <div
              className={`p-4 rounded-2xl text-xs space-y-2 animate-fade-in ${
                grammarFeedback.isCorrect
                  ? 'bg-emerald-950/60 border border-emerald-500/40 text-emerald-300'
                  : 'bg-rose-950/50 border border-rose-500/40 text-rose-200'
              }`}
            >
              <div className="font-bold flex items-center gap-2 text-sm">
                {grammarFeedback.isCorrect ? (
                  <>
                    <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />
                    <span>To'g'ri saralandi! (+15 XP)</span>
                  </>
                ) : (
                  <>
                    <XCircle className="w-5 h-5 text-rose-400 shrink-0" />
                    <span>Noto'g'ri! Tushuntirish:</span>
                  </>
                )}
              </div>
              <p className="text-slate-200 leading-relaxed">
                {grammarFeedback.rule}
              </p>
              {!grammarFeedback.isCorrect && (
                <div className="pt-2 flex justify-end">
                  <button
                    onClick={handleNextGrammar}
                    className="px-4 py-2 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-bold text-xs shadow-lg cursor-pointer"
                  >
                    Tushundim, keyingisi ▶️
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      )}

    </div>
  );
};
