function get(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.onload = function () {
        if (xhr.status === 200) {
            var json = JSON.parse(xhr.responseText);
            callback(json);
        }
        else {
            handleError(xhr);

        }
    };
    xhr.send();
}

function post(url, data, callback) {

    var xhr = new XMLHttpRequest();
    xhr.open('POST', url);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {

        if (xhr.status === 200) {
            try {
                var json = JSON.parse(xhr.responseText);
                callback(data, json);
            } catch (e) {
                handleClientError(e, url, json);
            }

        } else {

            handleError(xhr);
        }
    };
    xhr.send(JSON.stringify(data));
// xhr.send(JSON.stringify({
//     name: 'John Smith',
//     age: 34
// }));
}

function handleClientError(e, url, json) {
    console.log("error posting " + url);
    console.log("error " + e.toString());
    console.log("json returned " + json);
}

function handleError(xhr) {
    console.log("Exception on server " + xhr.status + ":" + xhr.statusText);
}

function dayOfWeek(json) {
    let labels = ["Sun", "Mon", "Tues", "Weds", "Thurs", "Fri", "Sat"];
    let values = [0, 0, 0, 0, 0, 0, 0];

    for (let i = 0; i < json.length; i++) {
        var epochSeconds = json[i].left;
        var localDate = new Date(0);
        localDate.setUTCSeconds(epochSeconds);
        var dayOfWeek = localDate.getDay();
        values[dayOfWeek] = values[dayOfWeek] + json[i].right;
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
    return data;
}

// var xhr = new XMLHttpRequest();
// xhr.open('GET', 'myservice/username?id=some-unique-id');
// xhr.onload = function () {
//     if (xhr.status === 200) {
//         alert('User\'s name is ' + xhr.responseText);
//     }
//     else {
//         alert('Request failed.  Returned status of ' + xhr.status);
//     }
// };
// xhr.send();
//
// var xhr = new XMLHttpRequest();
// xhr.open('PUT', 'myservice/user/1234');
// xhr.setRequestHeader('Content-Type', 'application/json');
// xhr.onload = function () {
//     if (xhr.status === 200) {
//         var userInfo = JSON.parse(xhr.responseText);
//     }
// };
// xhr.send(JSON.stringify({
//     name: 'John Smith',
//     age: 34
// }));
//
// function encode(object) {
//     var encodedString = '';
//     for (var prop in object) {
//         if (object.hasOwnProperty(prop)) {
//             if (encodedString.length > 0) {
//                 encodedString += '&';
//             }
//             encodedString += encodeURI(prop + '=' + object[prop]);
//         }
//     }
//     return encodedString;
// }