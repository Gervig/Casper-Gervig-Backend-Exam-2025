package app.controllers;

import java.util.List;

public interface ISkiLesson<T, I>
{
    List<T> getAllLessons();
    T getLessonById(I id);
    T createLesson(T lesson);
    T updateLesson(T lesson);
    void deleteLesson(I id);
    void addInstructor(I lessonId, I instructorId);
    void removeLesson(T lesson);
}
