package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Genre;

public interface GenreDao {
	public List<Genre> populateGenreSelect();
}
