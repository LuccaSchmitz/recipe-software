import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styled from "styled-components";
import { Link } from "react-router-dom";
import api from "../services/api";
import EditProfileModal from "./EditProfileModal";

interface User {
  id: string;
  username: string;
  email: string;
  photoUrl: string;
}

interface Recipe {
  id: string;
  title: string;
  description: string;
  photoUrl: string;
}

const UserProfile: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [user, setUser] = useState<User | null>(null);
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    api
      .get(`/api/users/${id}`)
      .then((response) => {
        setUser(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the user!", error);
      });

    api
      .get(`/api/users/${id}/recipes`)
      .then((response) => {
        setRecipes(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the user's recipes!", error);
      });
  }, [id]);

  const handleEditProfile = () => {
    setIsModalOpen(true);
  };

  if (!user) return <div>Loading...</div>;

  const localStorageUserId = localStorage.getItem("userId");

  return (
    <Container>
      <UserProfileHeader>
        <UserImageWrapper>
          {user.photoUrl ? (
            <UserImage src={user.photoUrl} alt={user.username} />
          ) : (
            <Placeholder>No Image Available</Placeholder>
          )}
        </UserImageWrapper>
        <UserInfo>
          <h1>{user.username}</h1>
          <p>{user.email}</p>
          {localStorageUserId === user.id && (
            <EditButton onClick={handleEditProfile}>Edit Profile</EditButton>
          )}
        </UserInfo>
      </UserProfileHeader>
      <h2>Recipes</h2>
      <RecipeGrid>
        {recipes.map((recipe) => (
          <RecipeCard key={recipe.id}>
            <RecipeLink to={`/recipes/${recipe.id}`}>
              {recipe.photoUrl ? (
                <RecipeImage src={recipe.photoUrl} alt={recipe.title} />
              ) : (
                <Placeholder>No Image Available</Placeholder>
              )}
              <RecipeContent>
                <RecipeTitle>{recipe.title}</RecipeTitle>
                <RecipeDescription>{recipe.description}</RecipeDescription>
              </RecipeContent>
            </RecipeLink>
          </RecipeCard>
        ))}
      </RecipeGrid>
      {user && (
        <EditProfileModal
          isOpen={isModalOpen}
          onRequestClose={() => setIsModalOpen(false)}
          user={user}
        />
      )}
    </Container>
  );
};

const Container = styled.div`
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
`;

const UserProfileHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 40px;
`;

const UserImageWrapper = styled.div`
  flex: 0 0 150px;
  width: 150px;
  height: 150px;
  overflow: hidden;
  border-radius: 50%;
  background-color: #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const UserImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const UserInfo = styled.div`
  margin-left: 20px;
  flex: 1;

  h1 {
    margin: 0 0 10px 0;
    font-size: 2rem;
    color: #333;
  }

  p {
    margin: 0;
    color: #666;
    font-size: 1rem;
  }
`;

const EditButton = styled.button`
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;

  &:hover {
    background-color: #0056b3;
  }
`;

const RecipeGrid = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  list-style: none;
  padding: 0;
`;

const RecipeLink = styled(Link)`
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
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

const RecipeImage = styled.img`
  width: 100%;
  height: 200px;
  object-fit: cover;
`;

const Placeholder = styled.div`
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ccc;
  color: #fff;
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

export default UserProfile;
