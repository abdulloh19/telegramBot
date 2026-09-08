/**
 * High-Reliability Audio & Speech System for Mnemonic WebApp
 * 
 * Multi-layer Audio Strategy:
 * 1. Primary: Next.js Same-Origin TTS API (/api/tts) — crystal clear native human audio, bypasses all CORS and WebView restrictions.
 * 2. Secondary: Web Speech API (window.speechSynthesis) with Chromium pause-recovery & voice selection.
 * 3. Sound FX: Synthesized Web Audio Oscillator chimes for instant feedback (0 bytes bandwidth).
 */

class AudioManager {
  private static instance: AudioManager;
  private audioCtx: AudioContext | null = null;
  private currentAudio: HTMLAudioElement | null = null;
  private isUnlocked: boolean = false;

  private constructor() {
    // Automatically register user gesture unlock listeners
    if (typeof window !== 'undefined') {
      const unlockHandler = () => {
        this.unlockAudio();
        window.removeEventListener('click', unlockHandler);
        window.removeEventListener('touchstart', unlockHandler);
      };
      window.addEventListener('click', unlockHandler, { once: true });
      window.addEventListener('touchstart', unlockHandler, { once: true });
    }
  }

  public static getInstance(): AudioManager {
    if (!AudioManager.instance) {
      AudioManager.instance = new AudioManager();
    }
    return AudioManager.instance;
  }

  /**
   * Unlocks Web Audio & HTML5 Audio restrictions common in Telegram WebApp & iOS
   */
  public unlockAudio(): void {
    if (this.isUnlocked || typeof window === 'undefined') return;

    try {
      const ctx = this.getAudioContext();
      if (ctx && ctx.state === 'suspended') {
        ctx.resume();
      }

      // Resume SpeechSynthesis if paused
      if ('speechSynthesis' in window && window.speechSynthesis.paused) {
        window.speechSynthesis.resume();
      }

      this.isUnlocked = true;
    } catch {
      // Audio unlocking failed or not needed
    }
  }

  private getAudioContext(): AudioContext | null {
    if (typeof window === 'undefined') return null;
    if (!this.audioCtx) {
      const AudioContextClass =
        window.AudioContext ||
        (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext;
      if (AudioContextClass) {
        this.audioCtx = new AudioContextClass();
      }
    }
    if (this.audioCtx && this.audioCtx.state === 'suspended') {
      this.audioCtx.resume().catch(() => {});
    }
    return this.audioCtx;
  }

  /**
   * Main speech method: Tries Server TTS first, then Web Speech API fallback.
   */
  public speak(text: string, lang: 'en' | 'ru', onEnd?: () => void): void {
    if (typeof window === 'undefined' || !text || !text.trim()) {
      if (onEnd) onEnd();
      return;
    }

    this.stop();
    this.unlockAudio();

    const cleanText = text.trim();
    const cleanLang = lang.toLowerCase().startsWith('ru') ? 'ru' : 'en';

    // 1. Try Server TTS API (Same-origin stream)
    const ttsUrl = `/api/tts?text=${encodeURIComponent(cleanText)}&lang=${cleanLang}`;
    const audio = new Audio(ttsUrl);
    this.currentAudio = audio;

    let hasEnded = false;
    const safeEnd = () => {
      if (!hasEnded) {
        hasEnded = true;
        this.currentAudio = null;
        if (onEnd) onEnd();
      }
    };

    // Safety timeout in case audio stalls
    const timeoutId = setTimeout(() => {
      safeEnd();
    }, 8000);

    audio.onended = () => {
      clearTimeout(timeoutId);
      safeEnd();
    };

    audio.onerror = () => {
      clearTimeout(timeoutId);
      // Upstream failed or offline, fall back to browser SpeechSynthesis
      this.speakWithSpeechSynthesis(cleanText, cleanLang, safeEnd);
    };

    const playPromise = audio.play();
    if (playPromise !== undefined) {
      playPromise.catch(() => {
        clearTimeout(timeoutId);
        // Autoplay blocked by WebView, fall back to browser SpeechSynthesis
        this.speakWithSpeechSynthesis(cleanText, cleanLang, safeEnd);
      });
    }
  }

  /**
   * Fallback speech using browser's built-in SpeechSynthesis
   */
  private speakWithSpeechSynthesis(text: string, lang: 'en' | 'ru', onEnd?: () => void): void {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) {
      if (onEnd) onEnd();
      return;
    }

    try {
      if (window.speechSynthesis.paused) {
        window.speechSynthesis.resume();
      }
      window.speechSynthesis.cancel();

      const utterance = new SpeechSynthesisUtterance(text);
      utterance.lang = lang === 'ru' ? 'ru-RU' : 'en-US';
      utterance.rate = lang === 'ru' ? 0.9 : 0.85;
      utterance.pitch = 1.0;

      const voices = window.speechSynthesis.getVoices();
      if (voices.length > 0) {
        const match = voices.find(v => v.lang.toLowerCase().startsWith(lang));
        if (match) {
          utterance.voice = match;
        }
      }

      utterance.onend = () => {
        if (onEnd) onEnd();
      };

      utterance.onerror = () => {
        if (onEnd) onEnd();
      };

      // Fallback timer in case speech synthesis never fires onend
      setTimeout(() => {
        if (onEnd) onEnd();
      }, 4000);

      window.speechSynthesis.speak(utterance);
    } catch {
      if (onEnd) onEnd();
    }
  }

  public stop(): void {
    if (typeof window !== 'undefined') {
      if ('speechSynthesis' in window) {
        try {
          window.speechSynthesis.cancel();
        } catch {}
      }
      if (this.currentAudio) {
        try {
          this.currentAudio.pause();
          this.currentAudio.currentTime = 0;
        } catch {}
        this.currentAudio = null;
      }
    }
  }

  /**
   * Synthesizes pleasant UI sounds using Web Audio API
   */
  public playSuccessSound(): void {
    const ctx = this.getAudioContext();
    if (!ctx) return;

    try {
      const now = ctx.currentTime;
      const osc1 = ctx.createOscillator();
      const osc2 = ctx.createOscillator();
      const gain = ctx.createGain();

      osc1.type = 'sine';
      osc2.type = 'triangle';

      osc1.frequency.setValueAtTime(523.25, now); // C5
      osc1.frequency.exponentialRampToValueAtTime(659.25, now + 0.1); // E5
      osc1.frequency.exponentialRampToValueAtTime(783.99, now + 0.2); // G5

      osc2.frequency.setValueAtTime(1046.5, now + 0.15); // C6

      gain.gain.setValueAtTime(0.18, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.4);

      osc1.connect(gain);
      osc2.connect(gain);
      gain.connect(ctx.destination);

      osc1.start(now);
      osc2.start(now + 0.15);
      osc1.stop(now + 0.35);
      osc2.stop(now + 0.4);
    } catch {}
  }

  public playErrorSound(): void {
    const ctx = this.getAudioContext();
    if (!ctx) return;

    try {
      const now = ctx.currentTime;
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(220, now);
      osc.frequency.exponentialRampToValueAtTime(140, now + 0.22);

      gain.gain.setValueAtTime(0.15, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.25);

      osc.connect(gain);
      gain.connect(ctx.destination);

      osc.start(now);
      osc.stop(now + 0.25);
    } catch {}
  }

  public playClickSound(): void {
    const ctx = this.getAudioContext();
    if (!ctx) return;

    try {
      const now = ctx.currentTime;
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      osc.type = 'sine';
      osc.frequency.setValueAtTime(880, now);
      osc.frequency.exponentialRampToValueAtTime(440, now + 0.04);

      gain.gain.setValueAtTime(0.08, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.05);

      osc.connect(gain);
      gain.connect(ctx.destination);

      osc.start(now);
      osc.stop(now + 0.05);
    } catch {}
  }
}

export const audioManager = AudioManager.getInstance();
