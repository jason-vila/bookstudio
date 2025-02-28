package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.LiteraryGenre;
import utils.MySqlConexion;

public class LiteraryGenreDaoImpl implements LiteraryGenreDao {

    @Override
    public List<LiteraryGenre> populateLiteraryGenreSelect() {
        List<LiteraryGenre> literaryGenreList = new ArrayList<>();
        
        String sql = """
            SELECT LiteraryGenreID, GenreName
            FROM LiteraryGenres
        """;
        
        try (Connection cn = MySqlConexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                LiteraryGenre literaryGenre = new LiteraryGenre();
                literaryGenre.setLiteraryGenreId(rs.getString("LiteraryGenreID"));
                literaryGenre.setGenreName(rs.getString("GenreName"));
                literaryGenreList.add(literaryGenre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return literaryGenreList;
    }
}
