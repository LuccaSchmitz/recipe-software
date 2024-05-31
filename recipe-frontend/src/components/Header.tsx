import React from "react";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { useAuth } from "../contexts/AuthContext";
import api from "../services/api";

const Header: React.FC = () => {
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await api.post("/api/auth/logout", {});
      logout();
      navigate("/login");
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  return (
    <StyledHeader>
      <Logo to="/">Recipes</Logo>
      {token && (
        <Menu>
          <MenuItem>
            <NavLink to="/">Home</NavLink>
          </MenuItem>
          <MenuItem>
            <NavLink to={`/user/${localStorage.getItem("userId")}`}>
              My Profile
            </NavLink>
          </MenuItem>
          <MenuItem>
            <LogoutButton onClick={handleLogout}>Logout</LogoutButton>
          </MenuItem>
        </Menu>
      )}
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #333;
  padding: 0 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
`;

const Logo = styled(Link)`
  font-size: 1.8em;
  color: #fff;
  text-decoration: none;
  font-weight: bold;

  &:hover {
    color: #f0f0f0;
  }
`;

const Menu = styled.ul`
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
  gap: 20px;
`;

const MenuItem = styled.li`
  margin: 0;
`;

const NavLink = styled(Link)`
  color: #fff;
  text-decoration: none;
  font-size: 1em;
  transition: color 0.3s;

  &:hover {
    color: #f0f0f0;
  }
`;

const LogoutButton = styled.button`
  background: none;
  border: none;
  color: #fff;
  font-size: 1em;
  cursor: pointer;
  transition: color 0.3s;

  &:hover {
    color: #f0f0f0;
  }
`;

export default Header;
