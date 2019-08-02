/*	for message alert*/

window.setTimeout(function() {
    $(".alert").fadeOut("slow").slideUp(500, function()
    {
    	 $(this).remove();  // removing alert     
    });
}, 3000);

$(document).ready(function(){
	$("#setting").hover(function(){
		$("#icon").toggleClass("fa-spin");
	});
});

$(document).ready(function() {
    $('#mytable').DataTable();
} );


	if ($(window).width() < 768) {
		$('#nav-menu-container').removeClass('nav-mar');
		$('#mobile-nav').removeClass('nav-mar');
		$('#logo').css("margin-left","0px");
	} else {
		$('#nav-menu-container').addClass('nav-mar');
		
	}
