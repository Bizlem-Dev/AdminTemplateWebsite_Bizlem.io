$(function(){
  $('[data-toggle="tooltip"]').tooltip();
  $('#learnMore li:first-child a').tab('show')  
  });
  $(window).load(function(){
    $('.flexslider').flexslider({
      animation: "slide",
      controlNav: "thumbnails",
      start: function(slider){
        $('body').removeClass('loading');
      }
    });
    $(".flex-control-thumbs li img").hover(function(){
      $(this).click();
   });
  });