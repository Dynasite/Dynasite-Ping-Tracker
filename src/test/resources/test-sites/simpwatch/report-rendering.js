/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <https://unlicense.org>
 */

/**
 * List of colors for a given simp grade (i.e. A/B/C/D/F)
 *
 * @type {{A: string, B: string, C: string, D: string, F: string}}
 */
let gradings = {
    "A": "green",
    "B": "darkblue",
    "C": "goldenrod",
    "D": "orangered",
    "F": "firebrick"
}

/**
 * Sets up basic report content, such as titles, message,
 * colors, and toggleable elements.
 *
 * @param simp the name of the simp.
 * @param data the data contained in the report of the simp.
 */
function setupReport(simp, data) {
    //Title
    $("#simp-name").html(simp)
    $("#simp-description").html(data.description)

    if(data['simping-spree'] === true)
        $('#simping-spree').show()

    //Grade
    let grade = data.grading.grade
    let gradeColor = gradings[grade.substr(0, 1)]

    $("#simp-grading").html(gradeColor === undefined ? null : grade)
    $('#simp-grading').css("color", gradeColor === undefined ? "black" : gradeColor)


    $("#simp-comment").html(data.grading.comment)
}

/**
 * Sets up the different charts and graphs displayed in the report.
 * Done separately from the report setup as charts must be loaded
 * first.
 *
 * @param simp the name of the simp.
 * @param data the data contained in the report of the simp.
 */
function setupReportCharts(simp, data) {
    //Activity
    new google.charts.Bar(document.getElementById('simp-activity')).draw(
        google.visualization.arrayToDataTable([
            ["Year",            simp,           "Average Twitch Simp",   "Average Virgin Simp",     "Average Simp"],
            ["Current Year",    data.activity.current,           9.50,                    4.75,               3.15],
            ["Previous Year",   data.activity.previous,         10.46,                    3.25,               1.75]
        ]), {"legend": "none"})

    //History
    let historyData = [["Year", "Simp Activity"]]
    for(let entry in data.history.graph) {
        historyData.push([entry, data.history.graph[entry]])
    }

    new google.charts.Line(document.getElementById('simp-history')).draw(
        google.visualization.arrayToDataTable(historyData),
        {"legend": "bottom", height: data.history.height}
    )
}