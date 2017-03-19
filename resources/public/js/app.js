(function (win) {
  var templateQuiz = {
    "video":"youtube.com/sgjdhfj",
    "name":"Quiz 1",
    "nQuestions":2,
    "questions":{
      "1":{
        "q":"What the heck?",
        "a":{
          "1":"Answer 1",
          "2":"Answer 2",
          "3":"Correct Answer 3",
          "4":"Answer 4"
         },
         "c":"3"
      },
      "2":{
         "q":"What the heck again?",
         "a":{
          "1":"Answer 1",
          "2":"Correct Answer 2",
          "3":"Answer 3",
          "4":"Answer 4"
         },
         "c":2
      }
    }
  };


  $('#submit-quiz').on('click', function(e) {
    e.preventDefault();
    submitQuiz();
  });

  $('#addNewChoiceBtn').on('click', function(e) {
    e.preventDefault();
    appendNewChoiceContainer();
  });

  $('#addNewQuestionBtn').on('click', function(e) {
    e.preventDefault();
    appendNewQuestionContainer();
  })

  function submitQuiz() {
    var quiz = {};
    quiz.name = $("input[name=quiz_name]")[0].value;
    quiz.video = $("input[name=quiz_video]")[0].value;
    quiz.nQuestions = $('quiz-question-container').length;
    quiz.questions = {};
    for (var i = 1; i <= quiz.nQuestions; i++) {
      quiz.questions["q"+i] =  {
        "label" : "What the Heck",
        "a1": "Answer1",
        "a2": "Answer2",
        "c" : "a1"
      };
    }

    

  }

  function appendNewChoiceContainer() {

  }

  function appendNewQuestionContainer() {

  }

})(window);