import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import api from "../services/api";
import CreateRecipeModal from "./CreateRecipeModal";

interface Recipe {
  id: string;
  title: string;
  description: string;
  photoUrl: string;
}

const RecipeList: React.FC = () => {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState("");

  useEffect(() => {
    fetchRecipes();
  }, [searchTerm, sortBy]);

  const fetchRecipes = () => {
    api
      .get("/api/recipes", {
        params: {
          searchTerm,
          sortBy,
        },
      })
      .then((response) => {
        setRecipes(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the recipes!", error);
      });
  };

  const handleSaveRecipe = () => {
    setIsModalOpen(false);
    fetchRecipes();
  };

  return (
    <Container>
      <Title>
        Recipes
        <NewRecipeButton onClick={() => setIsModalOpen(true)}>
          Create New Recipe
        </NewRecipeButton>
      </Title>
      <Filters>
        <SearchInput
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Search recipes"
        />
        <SortSelect value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
          <option value="">Sort By</option>
          <option value="title">Title</option>
          <option value="createdAt">Created At</option>
        </SortSelect>
      </Filters>
      <RecipeGrid>
        {recipes.map((recipe) => (
          <RecipeCard key={recipe.id}>
            <RecipeLink to={`/recipes/${recipe.id}`}>
              {recipe.photoUrl ? (
                <RecipeImage src={recipe.photoUrl} alt={recipe.title} />
              ) : (
                <NoImage>No Image Available</NoImage>
              )}
              <RecipeContent>
                <RecipeTitle>{recipe.title}</RecipeTitle>
                <RecipeDescription>{recipe.description}</RecipeDescription>
              </RecipeContent>
            </RecipeLink>
          </RecipeCard>
        ))}
      </RecipeGrid>
      <CreateRecipeModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        onSave={handleSaveRecipe}
      />
    </Container>
  );
};

const Container = styled.div`
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
`;

const Title = styled.h1`
  text-align: center;
  margin-bottom: 40px;
  font-size: 2.5rem;
  color: #333;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const NewRecipeButton = styled.button`
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-left: 20px;
  margin-top: 20px;
  width: 200px;

  &:hover {
    background-color: #0056b3;
  }
`;

const Filters = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
`;

const SearchInput = styled.input`
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 50%;
`;

const SortSelect = styled.select`
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const RecipeGrid = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  list-style: none;
  padding: 0;
`;

const RecipeCard = styled.li`
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-10px);
  }
`;

const RecipeLink = styled(Link)`
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const RecipeImage = styled.img`
  width: 100%;
  height: 200px;
  object-fit: cover;
`;

const NoImage = styled.div`
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ccc;
  color: #666;
  font-size: 1.5rem;
`;

const RecipeContent = styled.div`
  padding: 20px;
  flex: 1;
`;

const RecipeTitle = styled.h2`
  margin: 0 0 10px 0;
  font-size: 1.5rem;
  color: #333;
`;

const RecipeDescription = styled.p`
  margin: 0;
  color: #666;
  font-size: 1rem;
`;

export default RecipeList;
