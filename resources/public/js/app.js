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

    $.ajax({
      url: "api/quiz",
      method: "POST",
      data: quiz
    }, function(data) {
      console.log(data);
    }, function(err) {
      console.log(err);
    });

  }

  function appendNewChoiceContainer() {

  }

  function appendNewQuestionContainer() {
    var currentNumberOfQuestions = $('quiz_question_label').length;
    $.ajax({
      url: 'templates/question.hbs',
      method: 'GET',
      cache: true
    }).done(function(data) {
        var source = $(data).html();
        var template = Handlebars.compile(source);
        var context = {
          question_label: "Question #" + currentNumberOfQuestions + " Label"
        };
        $("#addQuestionContainer").prepend(template(context));

    }, function(error) {
      console.log(error);
    })
  }

  $(document).ready(function() {
    // Handlebars Template
    var source   = $('#test-quiz').html();
    var template = Handlebars.compile(source);
    var context  = {
      num     : '1.',
      question: 'This is a test?',
      a1      : 'No',
      a2      : 'Yes',
      a3      : 'Maybe',
      a4      : '69'
    };
    for (var i = 0; i < 3; i++)
      $('#quizzes').append(template(context));

  });

})(window);
