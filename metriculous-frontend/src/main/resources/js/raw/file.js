var selectedFile = "";

function file(json) {
    let labels = [];
    let values = [];
    // console.log(JSON.stringify(json));
    for (let i = 0; i < json.length; i++) {
        labels.push(json[i].left);
        values.push(json[i].right);
    }
    let data = {
        labels: labels,
        datasets: [
            {
                title: "Some Data",
                values: values
            }
        ]
    };
    let chart = new frappe.Chart("#filesWithMostAuthorsChart", {
        title: "Most Authors per file",
        data: data,
        isNavigable: true,
        type: 'bar', // or 'line', 'scatter', 'pie', 'percentage'
        // height: 100,

        colors: ['#7cd6fd'],//, 'violet', 'blue'],
        // hex-codes or these preset colors;
        // defaults (in order):
        // ['light-blue', 'blue', 'violet', 'red',
        // 'orange', 'yellow', 'green', 'light-green',
        // 'purple', 'magenta', 'grey', 'dark-grey']

        format_tooltip_x: d => (d + '').toUpperCase(),
        format_tooltip_y: d => d + ' pts'
    });
    chart.parent.addEventListener('data-select', (e) => {
        document.getElementById("person-specific-info").style.display = "none";
        document.getElementById("file-specific-info").style.display = "grid";
        post(api + "/file/person" + queryString, labels[e.index], lineCountForFileByAuthor);
        post(api + "/file/time" + queryString, labels[e.index], lineCountForFileByTime);
        // console.log(JSON.stringify(e));
        // alert("show lines per author for file" + e.index + " " + e.label); // e contains index and value of current datapoint
    })
    ;
}

function lineCountForFileByTime(filename, json) {
    let chart = new frappe.Chart("#lineCountForFileByTime", {
        title: filename + ": Time of most lines editted ",
        data: dayOfWeek(json),
        isNavigable: true,
        type: 'bar', // or 'line', 'scatter', 'pie', 'percentage'
        // height: 100,

        colors: ['violet', 'blue'],
        // hex-codes or these preset colors;
        // defaults (in order):
        // ['light-blue', 'blue', 'violet', 'red',
        // 'orange', 'yellow', 'green', 'light-green',
        // 'purple', 'magenta', 'grey', 'dark-grey']

        format_tooltip_x: d => (d + '').toUpperCase(),
        format_tooltip_y: d => d + ' pts'
    });
    chart.parent.addEventListener('data-select', (e) => {
        // console.log(JSON.stringify(e));
        alert("Not yet implemented");
        // alert(e.index + " " + e.value); // e contains index and value of current datapoint
    });
}

function lineCountForFileByAuthor(filename, json) {
    let labels = [];
    let people = []
    let values = [];

    for (let i = 0; i < json.length; i++) {
        labels.push(json[i].person.name);
        var tmp = new Object();
        tmp.name = json[i].person.name;
        tmp.email = json[i].person.email;
        people.push(tmp);
        values.push(json[i].value);
    }
    let data = {
        labels: labels,
        datasets: [
            {
                title: "Some Data",
                values: values
            }
        ]
    };

    let chart = new frappe.Chart("#lineCountForFileByAuthor", {
        title: filename + ": Line count by Author ",
        data: data,
        isNavigable: true,
        type: 'bar', // or 'line', 'scatter', 'pie', 'percentage'
        // height: 100,

        colors: ['violet', 'blue'],
        // hex-codes or these preset colors;
        // defaults (in order):
        // ['light-blue', 'blue', 'violet', 'red',
        // 'orange', 'yellow', 'green', 'light-green',
        // 'purple', 'magenta', 'grey', 'dark-grey']

        format_tooltip_x: d => (d + '').toUpperCase(),
        format_tooltip_y: d => d + ' pts'
    });

    // this is now working, set navigable to true above
    chart.parent.addEventListener('data-select', (e) => {
        selectedFile = filename;
        post(api + "/commit/file/person" + queryString + "&filename=" + filename, people[e.index], commitHistory);
    });
}

function commitHistory(person, json) {
    let tableStr = "<table class='metriculous-table'><tr><th colspan='2' text-align='center'>" + person.name + " : " + selectedFile + "</th></tr>";
    tableStr += "<table><tr><th colspan='1'>Timestamp</th><th>Hash</th></tr>";
    json.forEach(function (element) {
        let timestamp = new Date(element.left * 1000);
        tableStr += "<tr><td>" + timestamp.toLocaleString() + "</td><td>" + element.right + "</td></tr>";
    });
    tableStr += "</table>";
    document.getElementById("commit-history").innerHTML = tableStr;
}