import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styled from "styled-components";
import api from "../services/api";
import EditRecipeModal from "../components/EditRecipeModal";

interface Recipe {
  id: string;
  title: string;
  description: string;
  photoUrl: string;
  steps: string[];
  ingredients: string[];
  comments: string[];
  likes: string[];
  ownerId: string;
}

const RecipeDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [recipe, setRecipe] = useState<Recipe | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    api
      .get(`/api/recipes/${id}`)
      .then((response) => {
        setRecipe(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the recipe!", error);
      });
  }, [id]);

  const handleEdit = () => {
    setIsModalOpen(true);
  };

  const handleDelete = () => {
    api
      .delete(`/api/recipes/${id}`)
      .then(() => {
        navigate("/");
      })
      .catch((error) => {
        console.error("There was an error deleting the recipe!", error);
      });
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  const handleUpdate = () => {
    api
      .get(`/api/recipes/${id}`)
      .then((response) => {
        setRecipe(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the recipe!", error);
      });
  };

  if (!recipe) return <div>Loading...</div>;

  return (
    <Container>
      <Title>{recipe.title}</Title>
      {recipe.photoUrl ? (
        <Image src={recipe.photoUrl} alt={recipe.title} />
      ) : (
        <PlaceholderImage>No Image Available</PlaceholderImage>
      )}
      <Description>{recipe.description}</Description>
      <SectionTitle>Ingredients</SectionTitle>
      <List>
        {recipe.ingredients.map((ingredient, index) => (
          <ListItem key={index}>{ingredient}</ListItem>
        ))}
      </List>
      <SectionTitle>Steps</SectionTitle>
      <List>
        {recipe.steps.map((step, index) => (
          <ListItem key={index}>{step}</ListItem>
        ))}
      </List>
      {recipe.ownerId === localStorage.getItem("userId") && (
        <ButtonContainer>
          <Button type="edit" onClick={handleEdit}>
            Edit
          </Button>
          <Button type="delete" onClick={handleDelete}>
            Delete
          </Button>
        </ButtonContainer>
      )}
      {recipe && (
        <EditRecipeModal
          isOpen={isModalOpen}
          onRequestClose={handleModalClose}
          recipe={recipe}
          onUpdate={handleUpdate}
        />
      )}
    </Container>
  );
};

const Container = styled.div`
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  margin-bottom: 20px;
  text-align: center;
`;

const Image = styled.img`
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 20px;
`;

const PlaceholderImage = styled.div`
  width: 100%;
  min-height: 400px;
  max-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ccc;
  color: #fff;
  font-size: 1.5rem;
  border-radius: 10px;
  margin-bottom: 20px;
`;

const Description = styled.p`
  font-size: 1.2rem;
  margin-bottom: 20px;
`;

const SectionTitle = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 10px;
`;

const List = styled.ul`
  list-style-type: disc;
  padding-left: 20px;
  margin-bottom: 20px;
`;

const ListItem = styled.li`
  font-size: 1rem;
  margin-bottom: 5px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
`;

const Button = styled.button<{ type?: string }>`
  padding: 10px 20px;
  font-size: 1rem;
  background-color: ${(props) => (props.type === "edit" ? "#0056b3" : "red")};
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
`;

export default RecipeDetails;
