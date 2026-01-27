package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int countOfTrainings;

    public CounterOfTrainings(Coach coach, int countOfTrainings) {
        this.coach = coach;
        this.countOfTrainings = countOfTrainings;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCountOfTrainings() {
        return countOfTrainings;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.countOfTrainings, countOfTrainings);
    }
}
