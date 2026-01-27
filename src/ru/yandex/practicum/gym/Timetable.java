package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfTraining = trainingSession.getDayOfWeek();
        TimeOfDay timeOfTraining = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> scheduleOfDay = timetable.get(dayOfTraining);

        if (scheduleOfDay == null) {
            scheduleOfDay = new TreeMap<>();
            timetable.put(dayOfTraining, scheduleOfDay);
        }

        List<TrainingSession> listOfTrainingSession = scheduleOfDay.get(timeOfTraining);
        if (listOfTrainingSession == null) {
            listOfTrainingSession = new ArrayList<>();
            scheduleOfDay.put(timeOfTraining, listOfTrainingSession);
        }

        listOfTrainingSession.add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.get(dayOfWeek) == null) {
            return Collections.emptyMap();
        }
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> scheduleOfDay = timetable.get(dayOfWeek);
        if (scheduleOfDay == null){
            return Collections.emptyList();
        }
        return scheduleOfDay.get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> counterOfTrainings = new ArrayList<>();
        HashMap<Coach, Integer> trainingCounters = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, List<TrainingSession>> daySessions = getTrainingSessionsForDay(day);

            if (!daySessions.isEmpty()) {
                Collection<List<TrainingSession>> listOfTrainingSessions = daySessions.values();

                for (List<TrainingSession> sessionsList : listOfTrainingSessions) {
                    for (TrainingSession session : sessionsList) {
                        if (!trainingCounters.containsKey(session.getCoach())) {
                            trainingCounters.put(session.getCoach(), 1);
                        } else {
                            Integer trainingsCount = trainingCounters.get(session.getCoach());
                            trainingsCount++;
                            trainingCounters.put(session.getCoach(), trainingsCount);
                        }
                    }
                }
            }
        }
        for (Map.Entry<Coach, Integer> entry : trainingCounters.entrySet()) {
            counterOfTrainings.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        Collections.sort(counterOfTrainings);
        return counterOfTrainings;
    }
}
