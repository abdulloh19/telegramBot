export type TargetLanguage = 'en' | 'ru';

export type WordLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';

export type AppTab = 'dashboard' | 'lesson' | 'grammar' | 'games' | 'dialogue' | 'practice' | 'quiz' | 'search';

export type GameMode = 'flashcards' | 'match' | 'scramble' | 'grammarSort';

export interface MemoryCardItem {
  id: string;
  pairId: string;
  content: string;
  subContent?: string;
  type: 'target' | 'uzbek';
  isFlipped: boolean;
  isMatched: boolean;
}

export interface MnemonicWord {
  id: string;
  word: string;
  pronunciation: string;
  uzbekMeaning: string;
  mnemonicHook: string;
  mnemonicStory: string;
  exampleTarget: string;
  exampleUz: string;
  level: WordLevel;
  language: TargetLanguage;
}

export interface DialogueLine {
  speaker: string;
  speakerIcon: string;
  textTarget: string;
  textUz: string;
}

export interface DailyDialogue {
  id: string;
  title: string;
  situationTarget: string;
  situationUz: string;
  level: WordLevel;
  dayIndex: number;
  language: TargetLanguage;
  lines: DialogueLine[];
  targetWords: string[];
}

export interface ExerciseQuestion {
  id: string;
  prompt: string;
  options: string[];
  correctIndex: number;
  word: MnemonicWord;
  explanation: string;
}

export interface UserStats {
  streak: number;
  maxStreak: number;
  lastActiveDate: string;
  wordsLearnedCount: number;
  exercisesCompletedCount: number;
  quizScores: number;
  xp: number;
  todayCompleted: boolean;
}

export interface GrammarExample {
  target: string;
  uzbek: string;
  note?: string;
}

export interface GrammarQuiz {
  question: string;
  options: string[];
  correctIndex: number;
  explanation: string;
}

export interface GrammarTopic {
  id: string;
  title: string;
  titleUz: string;
  category: string;
  level: WordLevel;
  language: TargetLanguage;
  icon: string;
  mnemonicRule: string;
  detailedExplanation: string;
  formula?: string;
  examples: GrammarExample[];
  quiz: GrammarQuiz;
}

