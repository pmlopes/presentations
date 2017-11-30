
module.exports = function () {
    return new Promise(function (resolve, reject) {
        // fake that we go to a database and then...

        // 50% - 50% change
        if (Math.random() > 0.5) {
            /* everything turned out fine */
            resolve("Data from the database!");
        } else {
            /* that didn't went well */
            reject(Error("It broke"));
        }
    });
}