function calendar(json) {
    let chart = new frappe.Chart("#dayOfWeekChart", {
        title: "When lines committed",
        data: dayOfWeek(json),
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
        // console.log(JSON.stringify(e));
        alert("dayOfWeekEventListener" +e.index + " " + e.value
        ); // e contains index and value of current datapoint
    });
}