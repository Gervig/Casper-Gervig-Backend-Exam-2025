package app.daos;

import app.entities.SkiLesson;

import java.util.Set;

public interface ISkiLessonInstructorDAO<I>
{
    void addInstructorToSkiLesson(I lessonId, I instructorId);
    Set<SkiLesson> getSkiLessonsByInstructor(I instructorId);
}
