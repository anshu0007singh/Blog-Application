package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstraints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(
        name = "CRUD REST APIs For Post Resorce"
)
public class PostController {

    private PostService postservice;

    public PostController(PostService postservice) {
        this.postservice = postservice;
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API to save post into database"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createPost")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postdto){
        return new ResponseEntity<>(postservice.createPost(postdto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Posts REST API",
            description = "Get All Posts REST API to Get All Posts from  database"
    )
    @GetMapping("/getposts")
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstraints.DEFAULT_PAGE_NUMBER, required = false)int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstraints.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstraints.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstraints.DEFAULT_SORT_DIRECTION,required = false) String sortDir){
        return postservice.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }


    @Operation(
            summary = "Get Post By ID REST API",
            description = "Get Post By ID REST API to Get Post By ID from database"
    )
    @GetMapping("/getById/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable("id") long id){
        return ResponseEntity.ok(postservice.getPostById(id));
    }


    @Operation(
            summary = "Update Post REST API",
            description = "Update Post REST API to Update post into database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postdto , @PathVariable("id") long id){
        return ResponseEntity.ok(postservice.updatePostById(postdto,id));
    }


    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post REST API to Delete post from database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") long id){
        postservice.deletePostById(id);
        return ResponseEntity.ok("Post has been deleted Successfully");
    }

    //Build gellAllPostByCategories category RESTAPI
    @Operation(
            summary = "Get All Post By Categories REST API",
            description = "Get All Post By Categories REST API to get All Post By Categories from database"
    )
    @GetMapping("/getAllPostByCategories/{Id}")
    public ResponseEntity<List<PostDto>> getAllPostByCategories(@PathVariable("Id") long categoryId){
        return new ResponseEntity<>(postservice.getAllPostByCategory(categoryId),HttpStatus.OK);
    }

}
