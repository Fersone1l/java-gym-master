package ru.yandex.practicum.gym;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должна быть одна тренировка");

        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник не должно быть тренировок");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = getTimetable();

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должна быть одна тренировка");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        NavigableSet<TimeOfDay> times = ((TreeMap<TimeOfDay, List<TrainingSession>>) thursdaySessions).navigableKeySet();

        assertEquals(2, times.size(), "В четверг должно быть две тренировки по разному времени");

        List<TimeOfDay> timeList = new ArrayList<>(times);

        assertEquals(new TimeOfDay(13, 0), timeList.get(0), "Первым должно быть время 13:00");
        assertEquals(new TimeOfDay(20, 0), timeList.get(1), "Вторым должно быть время 20:00");

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник не должно быть тренировок");
    }

    private static Timetable getTimetable() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        return timetable;
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        NavigableSet<TimeOfDay> times =((TreeMap<TimeOfDay, List<TrainingSession>>) mondaySessions).navigableKeySet();

        assertEquals(1, times.size(), "В понедельник должно быть одно занятие");

        List<TimeOfDay> timeList = new ArrayList<>(times);

        assertEquals(new TimeOfDay(13, 0), timeList.get(0), "Должно быть время 13:00");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNotEquals(new TimeOfDay(14, 0), timeList.get(0), "Не должно быть время 14:00");
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Бокс", Age.ADULT, 60);

        // Две разные тренировки в одно и то же время в один день
        TrainingSession yogaSession = new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 30));
        TrainingSession boxSession = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 30));

        timetable.addNewTrainingSession(yogaSession);
        timetable.addNewTrainingSession(boxSession);

        // Проверяем, что в 18:30 есть обе тренировки
        List<TrainingSession> sessionsAt1830 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 30));

        assertTrue(sessionsAt1830.contains(yogaSession));
        assertTrue(sessionsAt1830.contains(boxSession));

        assertEquals(2, sessionsAt1830.size());
    }

    @Test
    void testSessionsAddedInRandomOrderShouldBeSorted() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Сидоров", "Алексей", "Викторович");
        Group group = new Group("Кроссфит", Age.ADULT, 90);

        // Добавляем тренировки в РАЗНОМ порядке
        TrainingSession evening = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 0));  // Вечер
        TrainingSession morning = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0));   // Утро
        TrainingSession afternoon = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 30)); // День

        // Добавляем не в хронологическом порядке
        timetable.addNewTrainingSession(evening);   // 19:00
        timetable.addNewTrainingSession(morning);   // 9:00
        timetable.addNewTrainingSession(afternoon); // 14:30

        Map<TimeOfDay, List<TrainingSession>> fridaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        NavigableSet<TimeOfDay> times =((TreeMap<TimeOfDay, List<TrainingSession>>) fridaySessions).navigableKeySet();

        List<TimeOfDay> timeList = new ArrayList<>(times);

        assertEquals(3, times.size());

        assertEquals(new TimeOfDay(9, 0), timeList.get(0), "Должно быть время 9:00");
        assertEquals(new TimeOfDay(14, 30), timeList.get(1), "Должно быть время 14:30");
        assertEquals(new TimeOfDay(19, 0), timeList.get(2), "Должно быть время 19:00");
    }

    @Test
    void testAddingNewSessionInOneDayDoNotCreatingInOtherDays() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertFalse(mondaySessions.isEmpty());

        for (DayOfWeek day : DayOfWeek.values()) {
            if (day != DayOfWeek.MONDAY) {
                Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.getTrainingSessionsForDay(day);
                assertTrue(daySessions.isEmpty(), "В день " + day + "не должно быть тренировок");
            }
        }
    }

    @Test
    void testIsOneCoachCounting() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counterOfTrainingsOfEmptyTimetable = timetable.getCountByCoaches();
        assertEquals(0, counterOfTrainingsOfEmptyTimetable.size());

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");


        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.SATURDAY, new TimeOfDay(13, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.THURSDAY, new TimeOfDay(16, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.SUNDAY, new TimeOfDay(7, 0)));

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        assertEquals(6, counterOfTrainings.getFirst().getCountOfTrainings());
    }

    @Test
    void testIsAllCoachesTrainingsCounted() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counterOfTrainingsOfEmptyTimetable = timetable.getCountByCoaches();
        assertEquals(0, counterOfTrainingsOfEmptyTimetable.size());

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Викторович");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.SATURDAY, new TimeOfDay(13, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(16, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SUNDAY, new TimeOfDay(7, 0)));

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        assertEquals(3, counterOfTrainings.size());
    }

    @Test
    void testIsSortOfCountingCoachesCorrect() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Викторович");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.SATURDAY, new TimeOfDay(13, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(16, 30)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SUNDAY, new TimeOfDay(7, 0)));

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        assertEquals("Иванов", counterOfTrainings.get(0).getCoach().getSurname());
        assertEquals("Сидоров", counterOfTrainings.get(1).getCoach().getSurname());
        assertEquals("Петрова", counterOfTrainings.get(2).getCoach().getSurname());
    }
}
