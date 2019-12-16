const friendSuggestion = {
    id: null,
    baseUrl: "",
    container: null,
    controller: null,
    egg: null,
    prev: null,
    spinner: null,
    next: null,
    descriptionField: null,
    userField: null,
    counter: 0,
    page: 0,
    page_size: 10,
    askForMore: true,
    initSuggestion: (Url) => {
        friendSuggestion.baseUrl = Url;
        friendSuggestion.container = document.getElementById("container");
        friendSuggestion.controller = document.getElementById("imagesController");
        friendSuggestion.spinner = document.getElementById("loader");
        friendSuggestion.next = document.getElementById("nextButton");
        friendSuggestion.prev = document.getElementById("previousButton");
        friendSuggestion.egg = document.getElementById("noImagesSection");
        friendSuggestion.descriptionField = document.getElementById("descriptionField");
        friendSuggestion.userField = document.getElementById("userField");
        friendSuggestion.initRecommendation();

    },
    initRecommendation: () => {
        // Probably need to await for second

        friendSuggestion.getRecommendation();


    },
    getRecommendation: () => {

        user.authFetch(friendSuggestion.baseUrl + "backend/friends/suggestion", {
            method: 'GET'
        }, (response) => {
            if (response.status == 400) {
                $('#criteriaModal').modal('show');
                container.classList.add("hidden");
                return;
            }
            if (response.status !== 200) {
                console.log("ERROR!");
                return;
            }
            response.json().then((body) => {
                container.classList.remove("hidden");
                friendSuggestion.id = body.id;
                friendSuggestion.userField.innerText = `${body.userName},
		  ${body.age} @ ${body.city}`;
                friendSuggestion.descriptionField.innerText = body.description;
                friendSuggestion.loadImages();

            });

        });

    },

    restartCarrousel: () => {
        friendSuggestion.egg.classList.add("hidden");
        friendSuggestion.egg.firstElementChild.classList.remove("active");


        while (friendSuggestion.controller.lastChild) {
            friendSuggestion.controller.lastChild.remove();
        }
        friendSuggestion.counter = 0;
        friendSuggestion.askForMore = true;
        friendSuggestion.page = 0;
        // loadDescription();
        friendSuggestion.initRecommendation();
    },
    loadImages: () => {
        // Show a spinner while we load it
        // Hide the next button till we process the request
        friendSuggestion.next.classList.add("hidden");
        friendSuggestion.spinner.classList.remove("hidden");
        // Todo , get userId of suggestedUser
        const url = friendSuggestion.baseUrl + `backend/images/carrusel/user/${friendSuggestion.id}?page=${friendSuggestion.page}`;
        user.authFetch(url, {
            method: 'GET'
        }, (response) => {

            if (response.status !== 200) {
                console.log("ERROR!");
                return;
            }
            response.json().then((body) => {


                let images = body.elements;
                let exists = body.existMoreElements;
                // If we dont have more images we make sure we dont make
                // more petitions
                if (!exists)
                    friendSuggestion.askForMore = false;

                // Add the images to the DOM
                images.forEach(image => {
                    let div = document.createElement("div");
                    let img = document.createElement("img");
                    div.classList.add("carousel-item");
                    img.classList.add("d-block");
                    img.classList.add("mx-auto");
                    img.src = "data:image/" + image.type + ";base64, " + image.data;
                    div.append(img);
                    friendSuggestion.controller.append(div);
                });
                // Only if we received more images we proceed
                // Move to the next image in case we had at least one more
                if (images.length >= 1) {
                    // This is the case where we adding more images to an
                    // existing carrusel or we are building a new one
                    if (friendSuggestion.counter + 1 === friendSuggestion.page_size) {
                        friendSuggestion.controller.children[friendSuggestion.counter].classList.remove("active");
                        friendSuggestion.counter++;
                    } else {
                        friendSuggestion.prev.classList.add("hidden");
                    }
                    friendSuggestion.controller.children[friendSuggestion.counter].classList.add("active");

                    if (images.length !== 1) {
                        friendSuggestion.next.classList.remove("hidden");
                    }



                }
                // We didnt receive any image
                else {
                    friendSuggestion.egg.classList.remove("hidden");
                    friendSuggestion.egg.firstElementChild.classList.add("active");
                    friendSuggestion.prev.classList.add("hidden");
                    friendSuggestion.next.classList.add("hidden");

                }

                if (friendSuggestion.controller.childElementCount < friendSuggestion.page_size) {
                    friendSuggestion.askForMore = false;
                    // Do Something special when user doens't have any image
                }
                // data + type -> string
                friendSuggestion.spinner.classList.add("hidden");
            });

        }, (errors) => {
            console.log("ERROR!");
        });
    },
    onNextPressed: () => {
        // Case when we move from the first Slide
        if (friendSuggestion.counter === 0) {
            friendSuggestion.prev.classList.remove("hidden");
        }
        // Case when we are returning to the last slide and we know is the last
        // one
        if (friendSuggestion.counter + 2 == friendSuggestion.controller.childElementCount && !friendSuggestion.askForMore) {
            friendSuggestion.next.classList.add("hidden");
        }
        // When we reach the last element
        if (friendSuggestion.controller.childElementCount === friendSuggestion.counter + 1 ) {
            if (!friendSuggestion.askForMore)
                return;
            friendSuggestion.page++;
            friendSuggestion.loadImages();

            // Fetch next page and append those images to the carrousel;
            // Show also a spinner in the meanwhile?

        } else {

            // At This poiunt we are sure we have more elements
            friendSuggestion.controller.children[friendSuggestion.counter].classList.remove("active");
            friendSuggestion.controller.children[++friendSuggestion.counter].classList.add("active");
        }
    },


    onPrevPressed: () => {
        // Case where we are returning to the initial slide
        if (friendSuggestion.counter === 1) {
            friendSuggestion.prev.classList.add("hidden");

        }
        // Case where we are coming from the last slide
        if (friendSuggestion.counter === friendSuggestion.controller.childElementCount - 1) {
            friendSuggestion.next.classList.remove("hidden");
        }
        friendSuggestion.controller.children[friendSuggestion.counter].classList.remove("active");
        friendSuggestion.controller.children[--friendSuggestion.counter].classList.add("active");
    },
    onLikePressed: () => {
        user.authFetch(friendSuggestion.baseUrl + "backend/friends/accept", {
            method: "POST",
            body: JSON.stringify({
                id: friendSuggestion.id
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }, (result) => friendSuggestion.onFinishedSuggestion(), friendSuggestion.onFinishedSuggestion);

    },
    onDislikePressed: () => {
        user.authFetch(friendSuggestion.baseUrl + "backend/friends/reject", {
            method: "POST",
            body: JSON.stringify({
                id: friendSuggestion.id
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }, (result) => friendSuggestion.onFinishedSuggestion(), friendSuggestion.onFinishedSuggestion);

    },
    onFinishedSuggestion: () => {
        friendSuggestion.restartCarrousel();
        
    },
    keyHandler: (evt) => {
        evt = evt || window.event;

        switch (evt.keyCode) {
            // Right Arrow
            case 39:
                if (!friendSuggestion.next.classList.contains("hidden")) {
                    friendSuggestion.onNextPressed();
                }
                break;
                // Left Arrow
            case 37:
                if (!friendSuggestion.prev.classList.contains("hidden")) {
                    friendSuggestion.onPrevPressed();
                }
                break;
                // "a"
            case 65:
                friendSuggestion.onLikePressed();
                break;
                // "d"
            case 68:
                friendSuggestion.onDislikePressed();
                break;
        }
    }
};