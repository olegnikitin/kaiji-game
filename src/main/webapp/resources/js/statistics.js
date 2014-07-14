/**
 * Created by Jayson on 24.04.14.
 */

function charts(element1, element2, url){

    var jsonRoundData = [];
    var jsonGameData = [];

    $.getJSON(url)
        .done(function( obj ) {

            $.each(obj, function (key, value) {

                if ((key =='win')
                 || (key =='lose')
                 || (key =='draw')) {
               jsonRoundData.push([key, value]);
            } else {
               jsonGameData.push([key, value]);
                }

            });

            var roundData = optRound.series[0];
            for (key in jsonRoundData) {
                roundData.data.push(jsonRoundData[key]);
            }

            var gameData = optGame.series[0];
            for (key in jsonGameData) {
                gameData.data.push(jsonGameData[key]);
            }

            Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function(color) {
                return {
                    radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
                    stops: [
                        [0, color],
                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                    ]
                };
            });

            $('#'+element1).highcharts(optRound);
            $('#'+element2).highcharts(optGame);
        })

     var optRound = {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: 'All rounds results distribution'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
         series: [{
             type: 'pie',
             data: [

             ]}]
     }

    var optGame = {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: 'All games'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            type: 'pie',
            data: [

            ]}]
    }


}



