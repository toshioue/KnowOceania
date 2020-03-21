package mobileos.usna.edu;

import java.io.Serializable;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this class is a custom object used to store the questions, answers, and two false
 *              answers for a quiz. this object is used when loading information from the Quiz table
 *               in the database
 *
 */
public class QuizInfo implements Serializable {
    // all the needed variables for Quizzes
    private String Question;
    private String Answer;
    private String FalseOne;
    private String FalseTwo;

    /**
     * Constructor for the QuizInfo
     * @param question
     * @param answer
     * @param falseOne
     * @param falseTwo
     */
    public QuizInfo(String question, String answer, String falseOne, String falseTwo) {
        Question = question;
        Answer = answer;
        FalseOne = falseOne;
        FalseTwo = falseTwo;
    }

    /**
     * BELOW ARE JUST ALL GETTERS!
     * @return
     */
    public String getQuestion() {
        return Question;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getFalseOne() {
        return FalseOne;
    }

    public String getFalseTwo() {
        return FalseTwo;
    }
}
