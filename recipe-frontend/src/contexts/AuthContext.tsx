import React, {
  createContext,
  useState,
  useEffect,
  useContext,
  ReactNode,
} from "react";
import api from "../services/api";

interface AuthContextType {
  userId: any;
  token: string | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  register: (
    username: string,
    email: string,
    password: string
  ) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token")
  );
  const [userId, setUserId] = useState<string | null>(
    localStorage.getItem("userId")
  );

  useEffect(() => {
    if (token) {
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
  }, [token, userId]);

  const login = async (username: string, password: string) => {
    const response = await api.post("/api/auth/signin", {
      username,
      password,
    });

    setUserId(response.data.userId);
    setToken(response.data.accessToken);
    localStorage.setItem("token", response.data.accessToken);
    localStorage.setItem("userId", response.data.userId);

    api.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${response.data.token}`;
  };

  const logout = async () => {
    await api.post("/api/auth/logout");
    setToken(null);
    setUserId(null);

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    delete api.defaults.headers.common["Authorization"];
  };

  const register = async (
    username: string,
    email: string,
    password: string
  ) => {
    await api.post("/api/auth/signup", { username, email, password });
  };

  return (
    <AuthContext.Provider value={{ userId, token, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
