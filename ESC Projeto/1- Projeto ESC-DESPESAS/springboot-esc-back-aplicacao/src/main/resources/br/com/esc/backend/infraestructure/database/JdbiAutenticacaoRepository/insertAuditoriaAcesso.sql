INSERT INTO tbd_AuditoriaAcesso
    (
    id_Acesso,
    id_Funcionario,
    ds_DataHoraLogin,
    id_Maquina
    )
VALUES (
    (SELECT (MAX(id_Acesso) + 1) FROM tbd_AuditoriaAcesso),
    :idFuncionario,
    :dataHoraLogin,
    :idMaquina
)