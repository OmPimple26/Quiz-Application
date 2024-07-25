import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

// Class to represent a quiz question
class Question {
    private String questionText;
    private List<String> options;
    private int correctAnswer;

    public Question(String questionText, List<String> options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

// Class to represent the quiz
class Quiz {
    private List<Question> questions;
    private int score;
    private int currentQuestionIndex;
    private Timer timer;
    private Scanner sc;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.score = 0;
        this.currentQuestionIndex = 0;
        this.timer = new Timer();
        this.sc = new Scanner(System.in);
    }

    public void start() {
        for (currentQuestionIndex = 0; currentQuestionIndex < questions.size(); currentQuestionIndex++) {
            displayQuestion(questions.get(currentQuestionIndex));
            boolean answered = getAnswerWithinTime(10);
            if (!answered) {
                System.out.println("Time's up!");
            }
        }
        displayResults();
    }

    private void displayQuestion(Question question) {
        System.out.println("Question " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    private boolean getAnswerWithinTime(int seconds) {
        boolean[] answered = {false};
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!answered[0]) {
                    System.out.println("\nYou didn't answer in time.");
                }
                timer.cancel();
            }
        };

        timer = new Timer();
        timer.schedule(task, seconds * 1000);

        new Thread(() -> {
            int userAnswer = sc.nextInt();
            answered[0] = true;
            if (userAnswer - 1 == questions.get(currentQuestionIndex).getCorrectAnswer()) {
                score++;
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect.");
            }
            timer.cancel();
        }).start();

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return answered[0];
    }

    private void displayResults() {
        System.out.println("\nQuiz Over! Your final score is: " + score + "/" + questions.size());
        System.out.println("Summary:");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println((i + 1) + ". " + q.getQuestionText());
            System.out.println("Correct answer: " + q.getOptions().get(q.getCorrectAnswer()));
        }
    }
}

// Main class to run the quiz application
public class QuizApplication {
    public static void main(String[] args) {
        List<Question> questions = new ArrayList<>();

        // Sample questions
        questions.add(new Question("What is the capital of India?",
                List.of("Mumbai", "Chennai", "New Delhi", "Bangalore"), 2));
        questions.add(new Question("Which is the national sport of our country?",
                List.of("Hockey", "Cricket", "Football", "Table Tennis"), 0));
        questions.add(new Question("What is the smallest prime number?",
                List.of("0", "1", "2", "3"), 2));

        Quiz quiz = new Quiz(questions);
        quiz.start();
    }
}
