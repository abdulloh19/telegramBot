import { NextResponse } from 'next/server';
import fs from 'fs';
import path from 'path';

// Force dynamic execution - never cache users data
export const dynamic = 'force-dynamic';
export const revalidate = 0;

export async function GET() {
  try {
    // Path to tgbot/data/users.json
    const possiblePaths = [
      path.resolve(process.cwd(), '../tgbot/data/users.json'),
      path.resolve(process.cwd(), '../../tgbot/data/users.json'),
      'c:\\Users\\parij\\Desktop\\modul-3\\modul_4\\tgbot\\tgbot\\data\\users.json',
    ];

    let rawData = null;
    let loadedPath = '';
    for (const p of possiblePaths) {
      if (fs.existsSync(p)) {
        try {
          rawData = fs.readFileSync(p, 'utf-8');
          if (rawData) {
            loadedPath = p;
            break;
          }
        } catch {
          // continue
        }
      }
    }

    const today = new Date();
    const todayStr = today.toISOString().split('T')[0];

    interface UserData {
      chatId: number;
      firstName?: string;
      lastName?: string;
      username?: string;
      phoneNumber?: string;
      targetLanguage?: string;
      selectedLevel?: string;
      currentStreak?: number;
      maxStreak?: number;
      totalWordsLearned?: number;
      currentDayIndex?: number;
      currentWordInDay?: number;
      lastActiveDate?: string;
      lastReminderDate?: string;
      enteredAfterReminder?: boolean;
    }

    let parsedUsers: Record<string, UserData> = {};

    if (rawData) {
      try {
        parsedUsers = JSON.parse(rawData);
      } catch (e) {
        console.error('Error parsing users.json:', e);
      }
    }

    // ONLY REAL BOT USERS - NO FAKE MOCK USERS!
    const userList = Object.values(parsedUsers).map((u) => {
      const lastActive = u.lastActiveDate || todayStr;
      const lastActiveDateObj = new Date(lastActive);
      const diffTime = Math.max(0, today.getTime() - lastActiveDateObj.getTime());
      const daysSince = Math.floor(diffTime / (1000 * 60 * 60 * 24));

      const isTodayActive = u.lastActiveDate === todayStr;
      const enteredAfter = !!u.enteredAfterReminder;

      let statusBadge = 'Faol';
      let statusColor = 'emerald';

      if (enteredAfter) {
        statusBadge = '🟣 Eslatmadan so\'ng kirdi';
        statusColor = 'purple';
      } else if (isTodayActive) {
        statusBadge = '🟢 Bugun kirdi (Faol)';
        statusColor = 'emerald';
      } else if (daysSince <= 1) {
        statusBadge = '🟡 Kecha kirdi (Kutilmoqda)';
        statusColor = 'amber';
      } else if (daysSince === 2) {
        statusBadge = '🟠 2 kun kirmadi (Qizil chiziq)';
        statusColor = 'orange';
      } else {
        statusBadge = `🔴 Xavf ostida (${daysSince} kun kirmadi)`;
        statusColor = 'rose';
      }

      // Real Telegram Username or Telegram Name + ID
      const hasRealUsername = Boolean(u.username && u.username.trim().length > 0);
      const cleanUsername = hasRealUsername ? `@${u.username!.replace('@', '')}` : null;
      const fullName = [u.firstName, u.lastName].filter(Boolean).join(' ') || `User ${u.chatId}`;
      const displayIdentifier = cleanUsername || fullName;

      const isEnglish = (u.targetLanguage || '').toUpperCase().includes('EN');

      return {
        id: String(u.chatId),
        chatId: u.chatId,
        firstName: fullName,
        username: cleanUsername,
        hasUsername: hasRealUsername,
        displayIdentifier,
        phoneNumber: u.phoneNumber || null,
        language: isEnglish ? 'en' : 'ru',
        languageName: isEnglish ? 'Ingliz tili' : 'Rus tili',
        languageFlag: isEnglish ? '🇬🇧' : '🇷🇺',
        level: u.selectedLevel || 'A1-A2',
        streak: u.currentStreak || 0,
        wordsLearned: u.totalWordsLearned || 0,
        currentDayIndex: u.currentDayIndex || 1,
        currentWordInDay: u.currentWordInDay || 1,
        lastActiveDate: lastActive,
        daysSinceLastActive: daysSince,
        lastReminderDate: u.lastReminderDate || null,
        enteredAfterReminder: enteredAfter,
        statusBadge,
        statusColor,
      };
    });

    const activeToday = userList.filter(u => u.daysSinceLastActive === 0).length;
    const enteredAfter = userList.filter(u => u.enteredAfterReminder).length;

    return NextResponse.json(
      {
        success: true,
        totalUsers: userList.length,
        activeTodayCount: activeToday,
        enteredAfterReminderCount: enteredAfter,
        users: userList,
        loadedFrom: loadedPath,
        timestamp: new Date().toISOString(),
      },
      {
        status: 200,
        headers: {
          'Content-Type': 'application/json; charset=utf-8',
          'Cache-Control': 'no-store, no-cache, must-revalidate, proxy-revalidate, max-age=0',
        },
      }
    );
  } catch (error) {
    console.error('API Error in /api/users:', error);
    return NextResponse.json(
      { success: false, error: 'Foydalanuvchilar ma\'lumotlarini yuklashda xatolik' },
      { status: 500 }
    );
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const { wordIndex, language, level, chatId, firstName, lastName, username, phoneNumber } = body;

    const possiblePaths = [
      path.resolve(process.cwd(), '../tgbot/data/users.json'),
      path.resolve(process.cwd(), '../../tgbot/data/users.json'),
      'c:\\Users\\parij\\Desktop\\modul-3\\modul_4\\tgbot\\tgbot\\data\\users.json',
    ];

    let targetPath = '';
    let parsedUsers: Record<string, any> = {};

    for (const p of possiblePaths) {
      if (fs.existsSync(p)) {
        try {
          const raw = fs.readFileSync(p, 'utf-8');
          parsedUsers = JSON.parse(raw);
          targetPath = p;
          break;
        } catch {
          // continue
        }
      }
    }

    if (!targetPath) {
      targetPath = possiblePaths[0];
    }

    const todayStr = new Date().toISOString().split('T')[0];
    const targetChatId = chatId ? String(chatId) : '5787141744';

    if (!parsedUsers[targetChatId]) {
      parsedUsers[targetChatId] = {
        chatId: Number(targetChatId),
        firstName: firstName || 'Foydalanuvchi',
        lastName: lastName || undefined,
        username: username ? String(username).replace('@', '') : undefined,
        phoneNumber: phoneNumber || undefined,
        targetLanguage: language === 'en' ? 'ENGLISH' : 'RUSSIAN',
        selectedLevel: level || 'BEGINNER',
        currentStreak: 1,
        maxStreak: 1,
        lastActiveDate: todayStr,
        currentDayIndex: 1,
        currentWordInDay: 1,
        totalWordsLearned: 1,
        totalExercisesCompleted: 0,
        totalQuizCorrect: 0,
        totalQuizCount: 0,
        reminderEnabled: true,
        reminderHour: 20,
        botVersion: 4,
      };
    }

    const user = parsedUsers[targetChatId];
    if (firstName && (!user.firstName || user.firstName === 'Foydalanuvchi')) {
      user.firstName = firstName;
    }
    if (lastName && !user.lastName) {
      user.lastName = lastName;
    }
    if (username) {
      user.username = String(username).replace('@', '');
    }
    if (phoneNumber && !user.phoneNumber) {
      user.phoneNumber = phoneNumber;
    }
    if (typeof wordIndex === 'number' && wordIndex >= 0) {
      user.currentWordInDay = wordIndex + 1; // 1-based index (e.g. 5-so'z)
      user.totalWordsLearned = Math.max(user.totalWordsLearned || 0, user.currentWordInDay);
    }
    if (language) {
      user.targetLanguage = language === 'en' ? 'ENGLISH' : 'RUSSIAN';
    }
    if (level) {
      user.selectedLevel = level;
    }
    user.lastActiveDate = todayStr;

    // Persist to disk
    fs.writeFileSync(targetPath, JSON.stringify(parsedUsers, null, 2), 'utf-8');

    return NextResponse.json({
      success: true,
      currentWordInDay: user.currentWordInDay,
      chatId: targetChatId,
    });
  } catch (err) {
    console.error('Error updating user progress:', err);
    return NextResponse.json({ success: false, error: 'Xatolik yuz berdi' }, { status: 500 });
  }
}
