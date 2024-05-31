import React, { useState } from "react";
import Modal from "react-modal";
import styled from "styled-components";
import api from "../services/api";

const CreateRecipeModal = ({ isOpen, onRequestClose, onSave }: any) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [ingredients, setIngredients] = useState("");
  const [steps, setSteps] = useState("");
  const [photoUrl, setPhotoUrl] = useState("");

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    try {
      await api.post("/api/recipes", {
        title,
        description,
        ingredients: ingredients.split("\n"),
        steps: steps.split("\n"),
        ownerId: localStorage.getItem("userId"),
        photoUrl: photoUrl.length > 0 ? photoUrl : undefined,
      });

      setTitle("");
      setDescription("");
      setIngredients("");
      setSteps("");
      setPhotoUrl("");

      onSave();
    } catch (error) {
      console.error("Error creating recipe:", error);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      style={customStyles}
      contentLabel="Create Recipe Modal"
    >
      <Form onSubmit={handleSubmit}>
        <Label htmlFor="title">Title</Label>
        <Input
          type="text"
          id="title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <Label htmlFor="description">Description</Label>
        <Textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <Label htmlFor="ingredients">Ingredients</Label>
        <Textarea
          id="ingredients"
          value={ingredients}
          onChange={(e) => setIngredients(e.target.value)}
          rows={6}
        />
        <Label htmlFor="steps">Steps</Label>
        <Textarea
          id="steps"
          value={steps}
          onChange={(e) => setSteps(e.target.value)}
          rows={6}
        />
        <Label htmlFor="photoUrl">photo URL</Label>
        <Input
          type="text"
          id="photoUrl"
          value={photoUrl}
          onChange={(e) => setPhotoUrl(e.target.value)}
        />
        <Button type="submit">Create</Button>
      </Form>
    </Modal>
  );
};

const customStyles = {
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
    maxWidth: "400px",
    width: "100%",
    padding: "20px",
  },
};

const Form = styled.form`
  display: flex;
  flex-direction: column;
`;

const Label = styled.label`
  margin-bottom: 10px;
`;

const Input = styled.input`
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const Textarea = styled.textarea`
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical;
`;

const Button = styled.button`
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
`;

export default CreateRecipeModal;
