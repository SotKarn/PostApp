## PostApp

A simple app that uses https://jsonplaceholder.typicode.com/ API, to perform some basic operations (get all posts, create a post, delete a post) and lists data in a RecyclerView from local storage.

The app is based on a MVI architexture :
![architexture](https://user-images.githubusercontent.com/87072458/143219466-fd24f67b-039e-4705-b9ec-15a78ec75376.png)

### Flow
The first page is a fragment (PostFragment) with a RecyclerView that lists all posts from the cache. Once the fragment is launched it tries to get all posts from the remote and stores them in local storage with Room.

From the PostFragment, on an recyclerView item click, it navigates to another fragment (DetailsFragment), where you have the option to delete that particular post item (by pressing the floatingActionButton)

In addition, from the PostFragment you can create a new post by pressing the floatingActionButton, where you navigate to Fragment (CreatePostFragment).

### Libraries
Persisting data: Room v2.2.5 <br>
HTTP Client: Retrofit v2.9.0 <br>
Dependency Injection: Dagger-Hilt v2.38.1 <br>
