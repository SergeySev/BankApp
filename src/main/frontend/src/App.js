import React, {useState, useEffect} from "react";
import './App.css';
import axios from "axios";

const ClientProfiles = () => {
  const fetchClientProfiles = () => {
    axios.get("http://localhost:8080/api/v1/client").then(res => {
      console.log(res);
    });
  }

  useEffect(() => {
    fetchClientProfiles();
  }, [])
  return <h1>This is my frontend</h1>
}
function App() {
  return (
    <div className="App">
      <ClientProfiles />
    </div>
  );
}

export default App;
