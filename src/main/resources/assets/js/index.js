var TABLE_COLORS = [
    "#0095f9",
    "#9df900",
    "#00f9ec",
    "#f9f400",
    "#f99900",
    "#f90091",
    "#31f7a8",
    "#fcf805",
    "#14ffeb",
    "#ff2323"
];

function colorServers(){
    $(".basic-cs").each(function() {
        $(this).css("background-color", randomColor());
    });
    
    function randomColor(){
        return TABLE_COLORS[Math.floor(Math.random() * TABLE_COLORS.length)];
    }
}

$( document ).ready(function() {
    colorServers()
});