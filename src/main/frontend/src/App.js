import React, {useState, useEffect, useCallback} from "react";
import {useDropzone} from 'react-dropzone'
import './App.css';
import axios from "axios";

const ClientProfiles = () => {
    const [clientProfiles, setClientProfiles] = useState([]);
    const fetchClientProfiles = () => {
        axios.get("http://localhost:8080/api/v1/client").then(res => {
            setClientProfiles(res.data);
        });
    }

    useEffect(() => {
        fetchClientProfiles();
    }, [])

    return clientProfiles.map((clientProfile, index) => {
        return (
            <div key={index}>
                <h3>Name: {clientProfile.first_name} {clientProfile.last_name}</h3>
                <p>Id: {clientProfile.id} ID</p>
                <p>Status: {clientProfile.status}</p>
                <p>Email: {clientProfile.email}</p>
                <p>Created at: {clientProfile.created_at}</p>
                <MyDropzone {...clientProfile.id}/>
                <br/>
            </div>
        )
    })
};

function MyDropzone(client_id) {
    const onDrop = useCallback(acceptedFiles => {
        const file = acceptedFiles[0];

        const formData = new FormData();
        formData.append("file", file);

        axios.post(
            "http://localhost:8080/api/v1/client-document/" + client_id[0] + "/image/upload",
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }
        ).then(() => {
            console.log("File uploaded successfully");
        }).catch(err => {
            console.log(err);
        });

    }, []);
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop});

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the document file here ...</p> :
                    <p>Drag 'n' drop document file here, or click to select files</p>
            }
        </div>
    )
}

function App() {
    return (
        <div className="App">
            <ClientProfiles/>
        </div>
    );
}

export default App;
