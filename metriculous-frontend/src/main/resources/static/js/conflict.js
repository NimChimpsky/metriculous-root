var api = '/result/v1';
var queryString = '?start=0&end=20';

function init() {
    // console.log("config");
    get(api + "/conflict/file" + queryString, conflictedFileCallback);
    get(api + "/conflict/people" + queryString, conflictedPeopleCallback);

    var horizontalBarChartData = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [{
            label: 'Dataset 1',
            backgroundColor: 'red',
            borderColor: 'orange',
            borderWidth: 1,
            data: [1, 10, 3, 4, 0.5, 7, 2.3]
        }]
    };

    window.onload = function () {
        var ctx = document.getElementById('canvas').getContext('2d');
        window.myHorizontalBar = new Chart(ctx, {
            type: 'horizontalBar',
            data: horizontalBarChartData,
            options: {
                // Elements options apply to all of the options unless overridden in a dataset
                // In this case, we are setting the border of each horizontal bar to be 2px wide
                elements: {
                    rectangle: {
                        borderWidth: 2,
                    }
                },
                responsive: true,
                legend: {
                    position: 'right',
                },
                title: {
                    display: true,
                    text: 'Chart.js Horizontal Bar Chart'
                }
            }
        });

    };
}

function conflictedFileCallback(json) {
    console.log("file : " + json[0].left + " - " + json[0].right);
}

function conflictedPeopleCallback(json) {
    console.log("people " + json[0].person.name + " " + json[0].value);
}