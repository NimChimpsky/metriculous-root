function person(json) {
    let labels = [];
    let values = [];
    let people = [];

    for (let i = 0; i < json.length; i++) {
        labels.push(json[i].person.name);
        values.push(json[i].value);
        var person = {
            name: json[i].person.name,
            email: json[i].person.email
        };
        people.push(person);
    }

    let data = {
        labels: labels,
        datasets: [
            {
                title: "Some Data",
                values: values
            }
            // },
            // {
            //     title: "Another Set",
            //     values: [25, 50, -10, 15, 18, 32, 27, 14]
            // },
            // {
            //     title: "Yet Another",
            //     values: [15, 20, -3, -15, 58, 12, -17, 37]
            // }
        ]
    };
    let chart = new frappe.Chart("#linesInProdChart", {
        title: "Current number of lines In master",
        data: data,
        isNavigable: true,
        type: 'bar', // or 'line', 'scatter', 'pie', 'percentage'
        // height: 100,

        colors: ['#7cd6fd'],
        format_tooltip_x: d => (d + '').toUpperCase(),
        format_tooltip_y: d => d + ' pts'
    });

    chart.parent.addEventListener('data-select', (e) => {
        document.getElementById("person-specific-info").style.display = "grid";
        document.getElementById("file-specific-info").style.display = "none";
        post(api + "/person/file" + queryString, people[e.index], personlinesPerFileCallback);
        post(api + "/person/time" + queryString, people[e.index], linesCommitTime);
    })
    ;
}

function personlinesPerFileCallback(person, json) {

    let labels = [];
    let values = [];

    for (let i = 0; i < json.length; i++) {
        labels.push(json[i].label);
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

    let chart = new frappe.Chart("#linesPerFileForPersonChart", {
            title: person.name + ": Lines per file ",
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
        })
    ;
    chart.parent.addEventListener('data-select', (e) => {
        // e contains index and value of current datapoint
        console.log("personlinesPerFileCallback");
        console.log(JSON.stringify(e));
        console.log(JSON.stringify(person));
        post(api + "/commit/file/person" + queryString + "&filename=" + e.label, person, lineCountForFileByTime);
    })
    ;
}

function linesCommitTime(person, json) {
    // console.log("person " + JSON.stringify(person));
    // console.log("json " + JSON.stringify(json));
    let chart = new frappe.Chart("#linesCommitTimeByPersonChart", {
        title: person.name + ": Time of most lines editted ",
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
        alert(e.index + " " + e.value
        )
        ; // e contains index and value of current datapoint
    })
    ;
}

