// Javascript
// Javascript
var api = '/api/v1';
var queryString = '?start=0&end=20';

function repo(json) {
    // console.log("repo" + json);
    var str = "Showing stats for : " + json + "&nbsp;&nbsp;&nbsp; Click on charts to drill down";
    document.getElementById("repo-info").innerHTML = str;
}

function init() {
    // console.log("init");
    get(api + "/raw/person" + queryString, person);
    get(api + "/raw/file" + queryString, file);
    get(api + "/raw/time" + queryString, calendar);
    get(api + "/repo" + queryString, repo);
}

// function linesInCommitsByPerson(person, json) {
//     let labels = [];
//     let values = [];
//     for (let i = 0; i < json.length; i++) {
//         labels.push(json[i].label);
//         values.push(json[i].value);
//     }
//     let data = {
//         labels: labels,
//         datasets: [
//             {
//                 title: "Some Data",
//                 values: values
//             }
//         ]
//     };
//     let chart = new frappe.Chart("#linesInCommitsByPersonChart", {
//         title: person.name + ": Edited Lines in Commits ",
//         data: data,
//         isNavigable: false,
//         type: 'bar', // or 'line', 'scatter', 'pie', 'percentage'
//         // height: 100,
//
//         colors: ['violet', 'blue'],
//         // hex-codes or these preset colors;
//         // defaults (in order):
//         // ['light-blue', 'blue', 'violet', 'red',
//         // 'orange', 'yellow', 'green', 'light-green',
//         // 'purple', 'magenta', 'grey', 'dark-grey']
//
//         format_tooltip_x: d => (d + '').toUpperCase(),
//         format_tooltip_y: d => d + ' pts'
//     });
//     chart.parent.addEventListener('data-select', (e) => {
//         // console.log(JSON.stringify(e));
//         alert(e.index + " " + e.value); // e contains index and value of current datapoint
//     });
// }


