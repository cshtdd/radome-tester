export default function(jQuery){
    return {
        configure: function(formId){
            console.log('Configure send position');

            jQuery(`${formId} button`).click(function(e){
                console.log('button clicked');
            });
        }
    };
};