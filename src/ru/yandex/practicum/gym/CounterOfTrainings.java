package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    Coach coach;
    int countOfTrainings;

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
    public void addCountOfTrainings() {
        countOfTrainings++;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.countOfTrainings, countOfTrainings);
    }
}
