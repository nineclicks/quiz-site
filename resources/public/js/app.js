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
    quiz.name       = $("input[name=quiz_name]")[0].value;
    quiz.video      = $("input[name=quiz_video]")[0].value;
    quiz.nQuestions = $('quiz-question-container').length;
    quiz.questions  = {};

    for (var i = 1; i <= quiz.nQuestions; i++) {
      quiz.questions["q"+i] =  {
        "label" : "What the Heck",
        "a1"    : "Answer1",
        "a2"    : "Answer2",
        "c"     : "a1"
      };
    }

    $.ajax({
      url: "./api/quiz/Zpx1DblLfTKlFyTw",
      method: "GET"
    }).done(function(data) {
        buildQuizzes(data);


        // console.log(data);
        // $('#new-quiz').prepend(
        //   'Quiz url: '   + data['quiz-url']+'<br>',
        //   'Result url: ' + data['result-url']
        // );
        console.log(data);

    }, function(error) {
      console.log(error);
    });

  }

  function buildQuizzes(quiz) {
    var context;
    var source   = $('#test-quiz').html();
    var template = Handlebars.compile(source);

    context  = {
      question : quiz.questions.q1.q,
      a1       : quiz.questions.q1.a.a1,
      a2       : quiz.questions.q1.a.a2,
      a3       : quiz.questions.q1.a.a3,
      a4       : quiz.questions.q1.a.a4,
    };
    $('#new-quiz').prepend(template(context));

    context  = {
      quizName : quiz.name,
      question : quiz.questions.q2.q,
      a1       : quiz.questions.q2.a.a1,
      a2       : quiz.questions.q2.a.a2,
      a3       : quiz.questions.q2.a.a3,
      a4       : quiz.questions.q2.a.a4,
    };
    $('#new-quiz').prepend(template(context));


  }

  function appendNewChoiceContainer() {

  }

  function appendNewQuestionContainer() {
    $.ajax({
      url: 'templates/question.hbs',
      method: 'GET',
      cache: true
    }).done(function(data) {
        var source = $(data).html();
        var template = Handlebars.compile(source);
        var context = {};
        $("#addQuestionContainer").prepend(template(context));

    }, function(error) {
      console.log(error);
    });
  }

  // $(document).ready(function() {
  //   var source   = $('#test-quiz').html();
  //   var template = Handlebars.compile(source);
  //   var context  = {
  //     num     : '1.',
  //     question: 'This is a test?',
  //     a1      : 'No',
  //     a2      : 'Yes',
  //     a3      : 'Maybe',
  //     a4      : '69'
  //   };
  //   for (var i = 0; i < 3; i++)
  //     $('#new-quiz').append(template(context));
  //
  // });

})(window);
