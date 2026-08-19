package com.mnemonic.service;

import com.mnemonic.model.UserProfile;
import com.mnemonic.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StreakService {
    private final UserRepository userRepository;

    public StreakService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Foydalanuvchi faolligini qayd etadi va streakni hisoblaydi
     */
    public synchronized StreakResult recordDailyActivity(UserProfile profile) {
        LocalDate today = LocalDate.now();
        LocalDate lastActive = profile.getLastActiveDate();

        boolean streakIncreased = false;
        boolean streakReset = false;

        if (lastActive == null) {
            profile.setCurrentStreak(1);
            streakIncreased = true;
        } else if (lastActive.equals(today)) {
            // Bugun allaqachon faollik qilgan, streak o'zgarmaydi
            streakIncreased = false;
        } else if (lastActive.equals(today.minusDays(1))) {
            // Kecha kirgan, ketma-ketlik davom etadi!
            profile.setCurrentStreak(profile.getCurrentStreak() + 1);
            streakIncreased = true;
        } else {
            // 1 kundan ko'p o'tib ketgan, streak yangilanadi
            profile.setCurrentStreak(1);
            streakReset = true;
        }

        if (profile.getCurrentStreak() > profile.getMaxStreak()) {
            profile.setMaxStreak(profile.getCurrentStreak());
        }

        profile.setLastActiveDate(today);
        userRepository.save(profile);

        return new StreakResult(profile.getCurrentStreak(), profile.getMaxStreak(), streakIncreased, streakReset);
    }

    /**
     * Streak darajasi va unvonini qaytaradi
     */
    public String getStreakBadge(int streak) {
        if (streak >= 30) return "👑 Afsonaviy Poliglot (30+ kun)";
        if (streak >= 14) return "💎 Mnemonika Ustasi (14+ kun)";
        if (streak >= 7) return "🥇 Oltin Odat (7+ kun)";
        if (streak >= 3) return "🥈 Kuchli Shijoat (3+ kun)";
        if (streak >= 1) return "🥉 Ilk Qadam (1-2 kun)";
        return "🌱 Yangi Boshlovchi";
    }

    /**
     * Foydalanuvchi uchun chiroyli Streak kartasi
     */
    public String formatStreakCard(UserProfile profile) {
        String badge = getStreakBadge(profile.getCurrentStreak());
        LocalDate today = LocalDate.now();
        boolean activeToday = today.equals(profile.getLastActiveDate());

        StringBuilder sb = new StringBuilder();
        sb.append("🔥 <b>STREAK & NATIJALARINGIZ:</b>\n\n");
        sb.append("⚡ <b>Joriy Streak:</b> ").append(profile.getCurrentStreak()).append(" kun ketma-ket 🔥\n");
        sb.append("🏆 <b>Eng yuqori rekord:</b> ").append(profile.getMaxStreak()).append(" kun ⭐️\n");
        sb.append("🎖 <b>Darajangiz:</b> ").append(badge).append("\n\n");
        sb.append("📅 <b>Bugungi holat:</b> ").append(activeToday ? "✅ Bajarildi!" : "⏳ Hali bajarilmadi (Streakni saqlang!)").append("\n");
        sb.append("📚 <b>O'rganilgan so'zlar:</b> ").append(profile.getTotalWordsLearned()).append(" ta\n");
        sb.append("🎯 <b>Bajarilgan mashqlar:</b> ").append(profile.getTotalExercisesCompleted()).append(" ta\n\n");

        if (profile.getCurrentStreak() == 0 || !activeToday) {
            sb.append("💡 <i>Bugun 20 ta so'z darsini yoki mashqlarni bajaring va 🔥 Streak'ingizni oshiring!</i>");
        } else {
            sb.append("🎉 <i>Ajoyib natija! Ertaga ham davom etib yangi rekord o'rnating!</i>");
        }

        return sb.toString();
    }

    public static class StreakResult {
        private final int currentStreak;
        private final int maxStreak;
        private final boolean streakIncreased;
        private final boolean streakReset;

        public StreakResult(int currentStreak, int maxStreak, boolean streakIncreased, boolean streakReset) {
            this.currentStreak = currentStreak;
            this.maxStreak = maxStreak;
            this.streakIncreased = streakIncreased;
            this.streakReset = streakReset;
        }

        public int getCurrentStreak() {
            return currentStreak;
        }

        public int getMaxStreak() {
            return maxStreak;
        }

        public boolean isStreakIncreased() {
            return streakIncreased;
        }

        public boolean isStreakReset() {
            return streakReset;
        }
    }
}
