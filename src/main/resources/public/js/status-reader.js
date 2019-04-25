export default function(jQuery){
    function readStatus(config, completedCallback){
        function displayOutput(message){
            var updatedString = `Updated: ${new Date().toISOString()}`
            var finalOutput = `${message} <br /> ${updatedString}`;
            jQuery(config.containerId).html(finalOutput);
        }

        jQuery.ajax('/api/status', {
            contentType: 'application/json',
            type: 'GET'
        }).done(function(r){
            // console.log('Status read; response: ', r);
            displayOutput(r);
        }).fail(function(e) {
            // console.log('transmission error: ', e);
            var errorMessage = `[${e.status}] - ${e.statusText}`;
            displayOutput(errorMessage);
        }).always(function(){
            completedCallback();
        });
    }

    return {
        start: function(config){
            /*
            {
                containerId,
                intervalMs
            }
            */

            console.log('Position reader started');

            var statusReader = function(){
                 readStatus(config, function(){
                    setTimeout(statusReader, config.intervalMs);
                })
            };
            statusReader();
        }
    };
}